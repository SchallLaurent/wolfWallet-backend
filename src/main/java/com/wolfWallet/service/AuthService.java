package com.wolfWallet.service;

import com.wolfWallet.exception.DuplicateResourceException;
import com.wolfWallet.model.dto.AuthRequest;
import com.wolfWallet.model.dto.AuthResponse;
import com.wolfWallet.model.dto.RegisterRequest;
import com.wolfWallet.model.entity.User;
import com.wolfWallet.model.entity.UserRole;
import com.wolfWallet.repository.UserRepository;
import com.wolfWallet.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 🔐 Service d'authentification
 *
 * Gère :
 * - L'inscription des nouveaux utilisateurs
 * - La connexion avec génération de token JWT
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * 📝 Inscription d'un nouvel utilisateur
     */
    public AuthResponse register(RegisterRequest request) {

        // 1️⃣ Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Cet email est déjà utilisé : " + request.getEmail());
        }

        // 2️⃣ Créer le nouvel utilisateur
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // ✅ Hash du mot de passe
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCurrency(request.getCurrency() != null ? request.getCurrency() : "EUR");
        user.setRole(UserRole.USER); // ✅ Rôle par défaut
        user.setActive(true);

        // 3️⃣ Sauvegarder en base
        User savedUser = userRepository.save(user);

        // 4️⃣ Générer le token JWT
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name());

        // 5️⃣ Retourner la réponse avec le token
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .userId(savedUser.getId())
                .build();
    }

    /**
     * 🔑 Connexion d'un utilisateur existant
     */
    public AuthResponse login(AuthRequest request) {

        // 1️⃣ Authentifier l'utilisateur (vérifier email + password)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2️⃣ Si on arrive ici, l'authentification a réussi
        // Récupérer l'utilisateur depuis la base
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 3️⃣ Générer le token JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        // 4️⃣ Retourner la réponse avec le token
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }
}