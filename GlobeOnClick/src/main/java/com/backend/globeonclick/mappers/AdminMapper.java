package com.backend.globeonclick.mappers;

import com.backend.globeonclick.dto.request.AdminRequestDTO;
import com.backend.globeonclick.dto.response.AdminResponseDTO;
import com.backend.globeonclick.entity.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public Admin toEntity(AdminRequestDTO dto) {
        return Admin.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword()) // Aqui puedo encriptar la contraseña
                .adminNickName(dto.getAdminNickName())
                .role(dto.getRole())
                .adminState(dto.isAdminState())
                .build();
    }

    public AdminResponseDTO toDTO(Admin admin) {
        return AdminResponseDTO.builder()
                .adminId(admin.getAdminId())
                .name(admin.getName())
                .email(admin.getEmail())
                .adminNickName(admin.getAdminNickName())
                .role(admin.getRole())
                .adminState(admin.isAdminState())
                .build();
    }
}