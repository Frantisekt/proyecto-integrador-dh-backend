package com.backend.globeonclick.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String email;
    private String dni;
    private String newsletter;
}