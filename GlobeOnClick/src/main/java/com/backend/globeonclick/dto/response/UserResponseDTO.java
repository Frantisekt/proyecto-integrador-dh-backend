package com.backend.globeonclick.dto.response;

import com.backend.globeonclick.entity.Role;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String username;
    private String email;
    private String dni;
    private String newsletter;
    private Role role;
    private List<Long> favoritePackageIds;
}