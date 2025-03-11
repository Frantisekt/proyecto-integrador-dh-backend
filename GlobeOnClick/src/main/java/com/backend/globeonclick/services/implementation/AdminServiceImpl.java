package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.request.AdminRequestDTO;
import com.backend.globeonclick.dto.response.AdminResponseDTO;
import com.backend.globeonclick.entity.Admin;
import com.backend.globeonclick.entity.Role;
import com.backend.globeonclick.exception.ResourceConflictException;
import com.backend.globeonclick.exception.ResourceNotFoundException;
import com.backend.globeonclick.repository.IAdminRepository;
import com.backend.globeonclick.services.interfaces.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService {

    private final IAdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminResponseDTO createAdmin(AdminRequestDTO adminDTO) {
        // Validar email único
        if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
            throw new ResourceConflictException("Email ya registrado");
        }

        // Validar que el rol sea ADMIN o AGENT
        validateAdminRole(adminDTO.getRole());

        Admin admin = Admin.builder()
                .name(adminDTO.getName())
                .username(adminDTO.getUsername())
                .email(adminDTO.getEmail())
                .password(passwordEncoder.encode(adminDTO.getPassword()))
                .role(adminDTO.getRole())
                .state(adminDTO.isState())
                .type("ADMIN") // El tipo siempre es "ADMIN" para la entidad Admin
                .build();

        Admin savedAdmin = adminRepository.save(admin);
        return mapToDTO(savedAdmin);
    }


    @Override
    public AdminResponseDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado"));
        return mapToDTO(admin);
    }

    @Override
    public List<AdminResponseDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO adminDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado"));

        // Validar que el rol sea ADMIN o AGENT
        validateAdminRole(adminDTO.getRole());

        admin.setName(adminDTO.getName());
        admin.setUsername(adminDTO.getUsername());
        admin.setRole(adminDTO.getRole());
        admin.setState(adminDTO.isState());
        // No actualizamos el tipo, ya que siempre debe ser "ADMIN"

        if (adminDTO.getPassword() != null && !adminDTO.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        }

        Admin updatedAdmin = adminRepository.save(admin);
        return mapToDTO(updatedAdmin);
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Administrador no encontrado");
        }
        adminRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }

    private AdminResponseDTO mapToDTO(Admin admin) {
        return AdminResponseDTO.builder()
                .adminId(admin.getAdminId())
                .name(admin.getName())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .role(admin.getRole())
                .state(admin.isState())
                .type("ADMIN") // Aseguramos que el tipo siempre sea "ADMIN" en la respuesta
                .build();
    }

    private void validateAdminRole(Role role) {
        if (role != Role.ADMIN && role != Role.AGENT) {
            throw new IllegalArgumentException("El rol de un administrador debe ser ADMIN o AGENT");
        }
    }
}