package com.backend.globeonclick.authentication.response;

import com.backend.globeonclick.entity.Role;
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
    private String name;
    private String email;
    private Long userId; // Campo unificado para userId/adminId
    private String type; // "USER" o "ADMIN"
    private Role role;
    private String error;
}