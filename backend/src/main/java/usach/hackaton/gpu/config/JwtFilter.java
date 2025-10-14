package usach.hackaton.gpu.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import usach.hackaton.gpu.entities.AppUser;
import usach.hackaton.gpu.entities.AuthFactor;
import usach.hackaton.gpu.entities.Role;
import usach.hackaton.gpu.entities.UserStatus;
import usach.hackaton.gpu.repositories.AppUserRepository;
import usach.hackaton.gpu.repositories.AuthFactorRepository;
import usach.hackaton.gpu.repositories.RoleRepository;
import usach.hackaton.gpu.repositories.UserStatusRepository;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserStatusRepository statusRepository;
    private final AuthFactorRepository authFactorRepository;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil,
                     AppUserRepository userRepository,
                     RoleRepository roleRepository,
                     UserStatusRepository statusRepository,
                     AuthFactorRepository authFactorRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.authFactorRepository = authFactorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Omitir rutas públicas
        if (isPublicRoute(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.getEmail(token);

        // Buscar usuario
        AppUser user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Validar estado del usuario
        UserStatus status = statusRepository.findById(user.getStatusId()).orElse(null);
        if (status == null || !"ACTIVE".equalsIgnoreCase(status.getCode())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Validar rol
        Role role = roleRepository.findById(user.getRoleId()).orElse(null);
        if (role == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Validar AuthFactor vigente (opcional)
        List<AuthFactor> factors = authFactorRepository.findByUserId(user.getId());
        boolean hasValidFactor = factors.stream().anyMatch(f -> f.getExpirationDate().isAfter(LocalDate.now()));
        if (!hasValidFactor) {
            filterChain.doFilter(request, response);
            return;
        }

        // Construir authorities
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.getCode()));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicRoute(String uri) {
        return uri.startsWith("/auth/");
    }
}