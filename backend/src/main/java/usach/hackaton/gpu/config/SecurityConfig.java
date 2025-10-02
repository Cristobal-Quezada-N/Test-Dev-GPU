package usach.hackaton.gpu.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;

    public SecurityConfig(JwtFilter jwtFilter, CorsConfigurationSource corsConfigurationSource, ObjectMapper objectMapper) {
        this.jwtFilter = jwtFilter;
        this.corsConfigurationSource = corsConfigurationSource;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth -> {
                    configurePublicEndpoints(auth);
                    configureItemEndpoints(auth);
                    configureLoanEndpoints(auth);
                    configureUserEndpoints(auth);
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exeception -> exeception
                    .authenticationEntryPoint((request, response, exception) -> handleAuthenticationException(request, response, exception))
                    .accessDeniedHandler((request, response, exception) -> handleAccessDeniedException(request, response, exception))
                );
        return http.build();
    }

    private void handleAuthenticationException(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "Unathenticated", exception.getMessage());
    }

    private void handleAccessDeniedException(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException exception
    ) throws IOException {
        writeErrorResponse(request, response, HttpStatus.FORBIDDEN, "Forbidden", exception.getMessage());
    }

    private void writeErrorResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus httpStatus, String error, String message) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", request.getRequestURI());
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    private void configurePublicEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/api/auth/**").permitAll();
    }

    private void configureItemEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
            .requestMatchers("/api/items/getItems").hasAnyRole("ADMIN", "USER")
            .requestMatchers("/api/items/createItem").hasRole("ADMIN")
            .requestMatchers("/api/items/updateItem/**").hasRole("ADMIN")
            .requestMatchers("/api/items/deleteItem/**").hasRole("ADMIN")
            .requestMatchers("/api/items/**").hasAnyRole("ADMIN", "USER");
    }

    private void configureLoanEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
            .requestMatchers("/api/loans/createLoan").hasAnyRole("ADMIN", "USER")
            .requestMatchers("/api/loans/getLoans").hasAnyRole("ADMIN", "USER")
            .requestMatchers("/api/loans/rejectLoans/**").hasRole("ADMIN")
            .requestMatchers("/api/loans/acceptLoans/**").hasRole("ADMIN");
    }

    private void configureUserEndpoints(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth
            .requestMatchers("/api/users/me").hasAnyRole("ADMIN", "USER")
            .requestMatchers("/api/users/createUser").hasAnyRole("ADMIN", "USER")
            .requestMatchers("/api/users/status").hasAnyRole("ADMIN", "USER")
            .requestMatchers("/api/users/updateStatus/**").hasRole("ADMIN")
            .requestMatchers("/api/users/deleteUsers/**").hasRole("ADMIN")
            .requestMatchers("/api/users/deleteUser/**").hasRole("ADMIN")
            .requestMatchers("/api/users/getUsers").hasRole("ADMIN")
            .requestMatchers("/api/users/getUserByID/**").hasRole("ADMIN")
            .requestMatchers("/api/users/**").hasRole("ADMIN");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
