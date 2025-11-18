package usach.hackaton.gpu.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import usach.hackaton.gpu.enums.UserStatusCode;
import usach.hackaton.gpu.repositories.AppUserRepository;
import usach.hackaton.gpu.repositories.AuthFactorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AppUserRepository userRepository;
    private final AuthFactorRepository authFactorRepository;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Omitir rutas públicas
        if (isPublicRoute(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Omitir preflights
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug(
                "[JWT] Missing/invalid Authorization header: {} {}",
                request.getMethod(),
                request.getRequestURI()
            );
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isValid(token)) {
            log.debug("[JWT] Invalid token (signature/expiry): {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.getEmail(token);

        // Buscar usuario
        AppUser user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            filterChain.doFilter(request, response);
            log.debug("[JWT] No user for email: {}", email);
            return;
        }

        // Validar estado del usuario
        UserStatus status = user.getStatus();
        if (!UserStatusCode.ACTIVE.name().equals(status.getCode())) {
            filterChain.doFilter(request, response);
            log.debug("[JWT] Not active status for email: ", email);
            return;
        }

        // Validar rol
        Role role = user.getRole();
        if (role == null) {
            filterChain.doFilter(request, response);
            log.debug("[JWT] Missing role for email: {}", email);
            return;
        }

        // Validar AuthFactor vigente (opcional)
        List<AuthFactor> factors = authFactorRepository.findByUserId(user.getId());
        boolean hasValidFactor = factors.stream().anyMatch(f -> f.getExpirationDate().isAfter(LocalDateTime.now()));
        if (!hasValidFactor) {
            filterChain.doFilter(request, response);
            log.debug("[JWT] No valid AuthFactor for email: {}", email);
            return;
        }

        // Construir authorities
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.getCode()));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            user.getEmail(), null, authorities
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicRoute(HttpServletRequest request) {
        String uri = request.getServletPath();
        return uri.equals("/api/auth") || uri.startsWith("/api/auth/");
    }
}
