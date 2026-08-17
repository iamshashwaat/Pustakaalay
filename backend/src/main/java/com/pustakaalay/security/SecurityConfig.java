package com.pustakaalay.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
                "https://*.vercel.app",
                "http://localhost:5173"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type"
        ));

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/api/health/**",
                                "/api/auth/**"
                        ).permitAll()

                        // ADMIN-only user and role management
                        .requestMatchers(
                                "/api/roles/**",
                                "/api/users/**"
                        ).hasRole("ADMIN")

                        // BOOK WRITE OPERATIONS -> ADMIN ONLY
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/books/**",
                                "/api/book-copies/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/books/**",
                                "/api/book-copies/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/books/**",
                                "/api/book-copies/**"
                        ).hasRole("ADMIN")

                        // BOOK READ OPERATIONS -> ADMIN + MEMBER
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/books/**",
                                "/api/book-copies/**"
                        ).hasAnyRole("ADMIN", "MEMBER")

                        // BORROWING ACTIONS -> ADMIN ONLY
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/borrowings/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/borrowings/**"
                        ).hasAnyRole("ADMIN", "MEMBER")

                        // FINE ADMIN ACTIONS
                        .requestMatchers(
                                "/api/fines/*/paid",
                                "/api/fines/*/waive",
                                "/api/fines/process-overdue",
                                "/api/fines/borrowing/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/fines/**"
                        ).hasAnyRole("ADMIN", "MEMBER")

                        .requestMatchers(
                                "/api/reservations/**"
                        ).hasAnyRole("ADMIN", "MEMBER")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
