package com.backend.globeonclick.authentication.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type; // "USER" o "ADMIN"
    private String role; // null para USER, ADMIN o AGENT para admins
}