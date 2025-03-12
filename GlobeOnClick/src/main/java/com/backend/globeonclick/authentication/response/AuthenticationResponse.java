package com.backend.globeonclick.authentication.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String type;    // "USER" o "ADMIN"
    private String role;    // Rol específico del usuario
    private Long userId;    // ID del usuario (si es usuario normal)
    private Long adminId;   // ID del admin (si es administrador)
    private String username;
    private String email;
    private String name;
}
