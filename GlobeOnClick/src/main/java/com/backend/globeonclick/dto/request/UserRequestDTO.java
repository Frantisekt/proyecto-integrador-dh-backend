package com.backend.globeonclick.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String email;
    private String password;
    private String dni;
    private String newsletter;
}