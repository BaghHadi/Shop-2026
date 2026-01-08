package fr.esiea.shop2026.adapters.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Désactiver la protection CSRF (inutile pour une API REST stateless utilisée par un client console)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Autoriser l'accès à certaines URL sans connexion
                .authorizeHttpRequests(auth -> auth
                        // Autoriser Swagger UI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Autoriser l'inscription et le login
                        .requestMatchers("/users/register", "/users/login").permitAll()
                        // Autoriser la console H2
                        .requestMatchers("/h2-console/**").permitAll()
                         //Autoriser tout le reste (Pour ton dev actuel, sinon tu seras bloqué)
                        .anyRequest().permitAll()
                )

                // 3. Nécessaire pour que la console H2 s'affiche correctement (utilise des frames)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }

    // Ce Bean permet d'injecter le PasswordEncoder partout dans l'appli (UserService, etc.)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}