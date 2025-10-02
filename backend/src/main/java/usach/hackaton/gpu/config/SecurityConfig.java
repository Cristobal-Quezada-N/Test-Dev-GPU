package usach.hackaton.gpu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtFilter jwtFilter, CorsConfigurationSource corsConfigurationSource) {
        this.jwtFilter = jwtFilter;
        this.corsConfigurationSource = corsConfigurationSource;
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
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((req, res, e)-> {
                        res.setStatus(401);
                        res.setContentType("application/json");
                        res.getWriter().write("{\"error\":\"unauthenticated\",\"reason\":\"" + e.getMessage() + "\"}");
                    })
                    .accessDeniedHandler((req, res, e) -> {
                        res.setStatus(403);
                        res.setContentType("application/json");
                        res.getWriter().write("{\"error\":\"forbidden\",\"reason\":\"" + e.getMessage() + "\"}");
                    })
                );
        return http.build();
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
