package com.wolfWallet.controller;

import com.wolfWallet.model.dto.AuthRequest;
import com.wolfWallet.model.dto.AuthResponse;
import com.wolfWallet.model.dto.RegisterRequest;
import com.wolfWallet.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 🔐 Contrôleur d'authentification
 *
 * Endpoints publics pour :
 * - POST /api/auth/register - Inscription
 * - POST /api/auth/login - Connexion
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 📝 Inscription d'un nouveau compte
     *
     * Exemple de requête :
     * POST /api/auth/register
     * {
     *   "email": "john@example.com",
     *   "password": "password123",
     *   "firstName": "John",
     *   "lastName": "Doe",
     *   "currency": "EUR"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 🔑 Connexion à un compte existant
     *
     * Exemple de requête :
     * POST /api/auth/login
     * {
     *   "email": "john@example.com",
     *   "password": "password123"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * ✅ Tester si l'utilisateur est authentifié
     *
     * Exemple de requête :
     * GET /api/auth/me
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser() {
        return ResponseEntity.ok("Utilisateur authentifié !");
    }
}