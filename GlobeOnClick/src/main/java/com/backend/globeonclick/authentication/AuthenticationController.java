package com.backend.globeonclick.authentication;

import com.backend.globeonclick.authentication.request.AuthenticationRequest;
import com.backend.globeonclick.authentication.request.RegisterRequest;
import com.backend.globeonclick.authentication.response.AuthenticationResponse;

import com.backend.globeonclick.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @RequestBody RegisterRequest request,
            @RequestParam(defaultValue = "ADMIN") String role) {
        Role adminRole;
        try {
            adminRole = Role.valueOf(role.toUpperCase());
            // Validar que el rol sea válido para administradores
            if (adminRole != Role.ADMIN && adminRole != Role.AGENT) {
                return ResponseEntity.badRequest().body(
                        AuthenticationResponse.builder()
                                .error("Rol inválido para administrador. Debe ser ADMIN o AGENT")
                                .build()
                );
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    AuthenticationResponse.builder()
                            .error("Rol inválido: " + role)
                            .build()
            );
        }

        return ResponseEntity.ok(authenticationService.registerAdmin(request, adminRole));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);

        if (response.getError() != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }
}