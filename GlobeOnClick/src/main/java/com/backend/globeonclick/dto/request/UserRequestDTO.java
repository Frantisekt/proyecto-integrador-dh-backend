package com.backend.globeonclick.dto.request;

import com.backend.globeonclick.entity.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String username;
    private String email;
    private String password;
    private String dni;
    private String newsletter;
    private Role role;
}