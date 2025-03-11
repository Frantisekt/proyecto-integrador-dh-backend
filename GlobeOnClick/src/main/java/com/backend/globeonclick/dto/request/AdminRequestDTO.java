package com.backend.globeonclick.dto.request;

import com.backend.globeonclick.entity.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequestDTO {
    private String name;
    private String email;
    private String password;
    private String username;
    private Role role;      // Solo debe aceptar ADMIN o AGENT
    private boolean state;
}