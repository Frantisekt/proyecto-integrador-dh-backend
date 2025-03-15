package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.request.UserRequestDTO;
import com.backend.globeonclick.dto.response.FavoritePackagesResponseDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {
    UserResponseDTO createUser(UserRequestDTO userDTO);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Long id, UserRequestDTO userDTO);
    void deleteUser(Long id);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    UserResponseDTO getUserByDni(String dni);
    void addFavoritePackage(Long userId, Long packageId);
    void removeFavoritePackage(Long userId, Long packageId);
    FavoritePackagesResponseDTO getUserFavoritePackages(Long userId);
    Page<TourPackageResponseDTO> getUserFavoritePackagesPaginated(Long userId, int page, int size);
    boolean isPackageFavorite(Long userId, Long packageId);
}