package com.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/courses/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/courses/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/courses/*/lessons")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/lessons/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/lessons/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        .requestMatchers("/api/enrollments/**").hasRole("STUDENT")
                        .requestMatchers("/api/reviews/**").hasRole("STUDENT")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
}