package com.backend.globeonclick.dto.response;

import com.backend.globeonclick.entity.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponseDTO {
    private Long adminId;
    private String name;
    private String email;
    private String username;
    private Role role;
    private boolean state;  // Cambiado de adminState a state
    private String type;
}