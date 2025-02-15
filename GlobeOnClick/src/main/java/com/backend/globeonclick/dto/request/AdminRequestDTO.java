package com.backend.globeonclick.dto.request;

import com.backend.globeonclick.entity.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequestDTO {
    private String name;
    private String email;
    private String password;
    private String adminNickName;
    private Role role;
    private boolean adminState;
}