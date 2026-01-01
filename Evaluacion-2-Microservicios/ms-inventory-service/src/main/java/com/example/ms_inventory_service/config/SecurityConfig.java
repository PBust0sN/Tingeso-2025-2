package com.example.ms_inventory_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:https://192.168.39.157:30443/realms/toolRent}")
    private String issuerUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        System.out.println("Creating JwtDecoder bean with issuer URI: " + issuerUri);
        
        try {
            return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
        } catch (Exception e) {
            System.err.println("Error creating JwtDecoder with issuer location: " + e.getMessage());
            try {
                String jwksEndpoint = issuerUri.replaceAll("/realms/.*", "/realms/master/protocol/openid-connect/certs");
                System.out.println("Fallback: Using JWKS endpoint: " + jwksEndpoint);
                return NimbusJwtDecoder.withJwkSetUri(jwksEndpoint).build();
            } catch (Exception e2) {
                System.err.println("Failed to create fallback JwtDecoder: " + e2.getMessage());
                throw new IllegalArgumentException("Cannot create JwtDecoder bean", e);
            }
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource()) // habilita CORS
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("GET", "/api/images/**").permitAll() // Permitir GET de imágenes sin autenticación
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
                );

        return http.build();
    }

    public JwtAuthenticationConverter jwtAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

            if (realmAccess != null && realmAccess.get("roles") instanceof List<?>) {
                List<?> roles = (List<?>) realmAccess.get("roles");
                roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
            }

            return authorities;
        });
        return converter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://localhost:3000",
            "https://192.168.39.157",
            "https://192.168.39.157:32194",
            "https://192.168.39.157:30443",
            "https://192.168.39.157:8443",
            "https://gateway-service:8090"
        ));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Content-Type", "Authorization", "X-Total-Count"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}