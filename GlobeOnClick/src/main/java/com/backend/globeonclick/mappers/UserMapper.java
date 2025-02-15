package com.backend.globeonclick.mappers;

import com.backend.globeonclick.dto.request.UserRequestDTO;
import com.backend.globeonclick.dto.response.UserResponseDTO;
import com.backend.globeonclick.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        return User.builder()
                .name(dto.getName())
                .paternalSurname(dto.getPaternalSurname())
                .maternalSurname(dto.getMaternalSurname())
                .email(dto.getEmail())
                .password(dto.getPassword()) // Aqui puedo encriptar la contraseña
                .dni(dto.getDni())
                .newsletter(dto.getNewsletter())
                .build();
    }

    public UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .paternalSurname(user.getPaternalSurname())
                .maternalSurname(user.getMaternalSurname())
                .email(user.getEmail())
                .dni(user.getDni())
                .newsletter(user.getNewsletter())
                .build();
    }
}