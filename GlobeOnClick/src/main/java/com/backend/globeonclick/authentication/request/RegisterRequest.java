package com.backend.globeonclick.authentication.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String username;
    private String email;
    private String password;
    private String dni;
    private String newsletter;
}