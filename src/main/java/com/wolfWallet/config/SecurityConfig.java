package com.wolfWallet.config;

import com.wolfWallet.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 🔐 Configuration de la sécurité Spring Security avec JWT
 *
 * Cette configuration :
 * 1. Désactive CSRF (pas nécessaire avec JWT)
 * 2. Configure les endpoints publics et protégés
 * 3. Ajoute le filtre JWT pour valider les tokens
 * 4. Configure l'authentification avec la base de données
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Pour utiliser @PreAuthorize sur les méthodes
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * 🔑 Encodeur de mots de passe (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 🛡️ Configuration de la chaîne de filtres de sécurité
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactiver CSRF (pas nécessaire avec JWT)
                .csrf(AbstractHttpConfigurer::disable)

                // Configuration des autorisations
                .authorizeHttpRequests(auth -> auth
                        // ✅ Endpoints publics (accessibles sans authentification)
                        .requestMatchers(
                                "/api/auth/**",           // Inscription et connexion
                                "/api/health",            // Health check
                                "/h2-console/**",         // Console H2 (dev uniquement)
                                "/error"                  // Page d'erreur
                        ).permitAll()

                        // 🔒 Endpoints protégés (nécessitent un token JWT)
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/transactions/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/categories/**").hasAnyRole("USER", "ADMIN")

                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated()
                )

                // Configuration de la gestion de session (STATELESS pour JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Ajouter notre filtre JWT AVANT le filtre d'authentification standard
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Permettre l'affichage de la console H2 dans un iframe (dev uniquement)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );

        return http.build();
    }

    /**
     * 🔐 Fournisseur d'authentification avec notre UserDetailsService
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 🎯 Gestionnaire d'authentification (utilisé pour le login)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}