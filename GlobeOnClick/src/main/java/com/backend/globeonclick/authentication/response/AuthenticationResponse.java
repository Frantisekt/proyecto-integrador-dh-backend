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
    private String type; // "USER" o "ADMIN"
    private String role; // null para USER, ADMIN o AGENT para admins
}