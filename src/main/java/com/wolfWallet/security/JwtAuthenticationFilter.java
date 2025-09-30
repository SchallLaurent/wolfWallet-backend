package com.wolfWallet.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 🛡️ Filtre qui intercepte TOUTES les requêtes HTTP
 *
 * Ce filtre :
 * 1. Récupère le token JWT depuis le header "Authorization"
 * 2. Valide le token
 * 3. Authentifie l'utilisateur dans le SecurityContext
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1️⃣ Récupérer le header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2️⃣ Vérifier si le header contient un token Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3️⃣ Extraire le token (enlever "Bearer ")
        jwt = authHeader.substring(7);

        try {
            // 4️⃣ Extraire l'email depuis le token
            userEmail = jwtUtil.extractUsername(jwt);

            // 5️⃣ Vérifier si l'utilisateur n'est pas déjà authentifié
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6️⃣ Charger les détails de l'utilisateur
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // 7️⃣ Valider le token
                if (jwtUtil.validateToken(jwt, userDetails)) {

                    // 8️⃣ Créer un objet d'authentification
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 9️⃣ Mettre l'utilisateur dans le SecurityContext (= utilisateur authentifié)
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // En cas d'erreur (token invalide, etc.), on ne fait rien
            // La requête continuera mais l'utilisateur ne sera pas authentifié
        }

        // 🔟 Passer au filtre suivant
        filterChain.doFilter(request, response);
    }
}