package usach.hackaton.gpu.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
import usach.hackaton.gpu.entities.Role;
import usach.hackaton.gpu.entities.UserStatus;
import usach.hackaton.gpu.repositories.AppUserRepository;
import usach.hackaton.gpu.service.AuthFactorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    private final JwtUtil jwtUtil;
    private final AppUserRepository userRepository;
    private final AuthFactorService authFactorService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
        }

        // Verificar Token JWT
        String jwtToken = extractToken(request);

        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.isValid(jwtToken)) {
            log.debug("[JWT] Invalid token (signature/expiry): {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // Verificar usuario valido
        String userEmail = jwtUtil.getEmail(jwtToken);

        Optional<AppUser> validUserOpt = validUser(userEmail);
        if (validUserOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        AppUser user = validUserOpt.get();

        // Verificar metodos de autenticacion
        boolean hasValidFactor = validAuthFactor(user);
        if (!hasValidFactor) {
            filterChain.doFilter(request, response);
            return;
        }

        // Construir authorities
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getCode()));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            user.getEmail(), null, authorities
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.debug(
                "[JWT] Missing/invalid Authorization header: {} {}",
                request.getMethod(),
                request.getRequestURI()
            );
            return null;
        }
        return authHeader.substring(BEARER_PREFIX_LENGTH);
    }

    private boolean validAuthFactor(AppUser user) {
        boolean hasValidFactor = authFactorService.userHasValidAuthFactor(user);

        if (!hasValidFactor) {
            log.debug("[JWT] No valid AuthFactor for email: {}", user.getEmail());
        }

        return hasValidFactor;
    }

    private Optional<AppUser> validUser(String userEmail) {
        // Buscar usuario
        Optional<AppUser> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            log.debug("[JWT] No user for email: {}", userEmail);
            return Optional.empty();
        }

        AppUser user = optionalUser.get();

        // Validar estado del usuario
        UserStatus status = user.getStatus();
        if (!status.isActive()) {
            log.debug("[JWT] Not active status for email: ", userEmail);
            return Optional.empty();
        }

        // Validar rol
        Role role = user.getRole();
        if (role == null) {
            log.debug("[JWT] Missing role for email: {}", userEmail);
            return Optional.empty();
        }

        return Optional.of(user);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();

        // CORS Preflights
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // Public Routes
        return path.equals("/api/auth") || path.startsWith("/api/auth/");
    }
}
