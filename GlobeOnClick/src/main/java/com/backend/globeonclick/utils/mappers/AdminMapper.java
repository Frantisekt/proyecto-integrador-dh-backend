package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.AdminRequestDTO;
import com.backend.globeonclick.dto.response.AdminResponseDTO;
import com.backend.globeonclick.entity.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdminMapper {

    private final PasswordEncoder passwordEncoder;

    public AdminResponseDTO toResponseDTO(Admin admin) {
        if (admin == null) return null;

        return AdminResponseDTO.builder()
                .adminId(admin.getAdminId())
                .name(admin.getName())
                .email(admin.getEmail())
                .username(admin.getUsername())
                .role(admin.getRole())
                .state(admin.isState())
                .build();
    }

    public List<AdminResponseDTO> toResponseDTOList(List<Admin> admins) {
        return admins.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Admin toEntity(AdminRequestDTO requestDTO) {
        return Admin.builder()
                .name(requestDTO.getName())
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .role(requestDTO.getRole())
                .state(requestDTO.isState())
                .build();
    }

    public void updateEntity(Admin admin, AdminRequestDTO requestDTO) {
        admin.setName(requestDTO.getName());
        admin.setEmail(requestDTO.getEmail());
        admin.setUsername(requestDTO.getUsername());
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            admin.setPassword(requestDTO.getPassword());
        }
        admin.setRole(requestDTO.getRole());
        admin.setState(requestDTO.isState());
    }
}