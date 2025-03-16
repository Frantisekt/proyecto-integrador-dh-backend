package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.UserRequestDTO;
import com.backend.globeonclick.dto.response.UserResponseDTO;
import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) return null;

        List<Long> favoriteIds = new ArrayList<>();
        if (user.getFavoritePackages() != null && !user.getFavoritePackages().isEmpty()) {
            favoriteIds = user.getFavoritePackages().stream()
                    .map(TourPackage::getPackageId)
                    .collect(Collectors.toList());
        }

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .paternalSurname(user.getPaternalSurname())
                .maternalSurname(user.getMaternalSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .dni(user.getDni())
                .newsletter(user.getNewsletter())
                .role(user.getRole())
                .favoritePackageIds(favoriteIds)
                .build();
    }

    public List<UserResponseDTO> toResponseDTOList(List<User> users) {
        return users.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public User toEntity(UserRequestDTO requestDTO) {
        return User.builder()
                .name(requestDTO.getName())
                .paternalSurname(requestDTO.getPaternalSurname())
                .maternalSurname(requestDTO.getMaternalSurname())
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .dni(requestDTO.getDni())
                .newsletter(requestDTO.getNewsletter())
                .state(true)
                .role(requestDTO.getRole())

                .build();
    }

    public void updateEntity(User user, UserRequestDTO requestDTO) {
        user.setName(requestDTO.getName());
        user.setPaternalSurname(requestDTO.getPaternalSurname());
        user.setMaternalSurname(requestDTO.getMaternalSurname());
        user.setUsername(requestDTO.getUsername());
        user.setEmail(requestDTO.getEmail());
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            user.setPassword(requestDTO.getPassword()); // Note: Password should be encoded before saving
        }
        user.setDni(requestDTO.getDni());
        user.setNewsletter(requestDTO.getNewsletter());
        user.setRole(requestDTO.getRole());
    }


}