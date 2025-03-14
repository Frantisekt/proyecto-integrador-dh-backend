package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.request.UserRequestDTO;
import com.backend.globeonclick.dto.response.UserResponseDTO;
import com.backend.globeonclick.entity.Role;
import com.backend.globeonclick.entity.User;
import com.backend.globeonclick.exception.ResourceNotFoundException;
import com.backend.globeonclick.repository.IUserRepository;
import com.backend.globeonclick.services.interfaces.IUserService;
import com.backend.globeonclick.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        if (existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (existsByDni(userDTO.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        User user = userMapper.toEntity(userDTO);
        // Asegurarse de que el rol siempre sea USER
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseDTOList(users);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar email único si está cambiando
        if (!user.getEmail().equals(userDTO.getEmail()) && existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Verificar DNI único si está cambiando
        if (!user.getDni().equals(userDTO.getDni()) && existsByDni(userDTO.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        userMapper.updateEntity(user, userDTO);

        // Validar que el rol sea válido
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        user = userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(String dni) {
        return userRepository.existsByDni(dni);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByDni(String dni) {
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con DNI: " + dni));
        return userMapper.toResponseDTO(user);
    }
}