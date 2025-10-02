package usach.hackaton.gpu.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration c = new CorsConfiguration();
                        c.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173", "http://127.0.0.1:5173"));
                        c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
                        c.setAllowedHeaders(List.of("*"));
                        c.setAllowCredentials(true);
                        c.setMaxAge(3600L);
                        return c;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/items/createItem").hasAnyRole("ADMIN")
                        .requestMatchers("/api/items/getItems").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/items/updateItem/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/items/deleteItem/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/items/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/loans/getLoans").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/users/getUsers").hasAnyRole("ADMIN")
                        .requestMatchers("/api/users/deleteUsers/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/loans/rejectLoans/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/loans/acceptLoans/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/users/me").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/users/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/loans/createLoan").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/users/createUser").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/users/deleteUser/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/users/getUserByID/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/users/updateStatus/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/users/status").hasAnyRole("ADMIN", "USER")
                )
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}