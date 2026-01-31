package com.example.monolitico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    /**
     * Explicit SecurityFilterChain that permits all requests.
     * This overrides any auto-configured security and is intended
     * for debugging; remove when restoring real security.
     */
    @Bean
    public SecurityFilterChain permitAllSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .// ❌ nada de sesiones
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ❌ nada de CSRF
            .csrf(csrf -> csrf.disable())

            // ❌ nada de login form
            .formLogin(form -> form.disable())

            // ❌ nada de basic auth
            .httpBasic(basic -> basic.disable())

            // ❌ nada de logout
            .logout(logout -> logout.disable())

            // ❌ nada de request cache (ESTE ERA EL PROBLEMA)
            .requestCache(cache -> cache.disable())

            // ❌ nada de OAuth2
            .oauth2ResourceServer(oauth2 -> oauth2.disable())

            // ✅ todo permitido
            .authorizeHttpRequests(auth ->
                auth.anyRequest().permitAll()
            );

        return http.build();
    }

    /**
     * CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "https://toolrent-tingeso.duckdns.org",
            "http://toolrent-tingeso.duckdns.org",
            "http://localhost:5173"
        ));
        configuration.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
