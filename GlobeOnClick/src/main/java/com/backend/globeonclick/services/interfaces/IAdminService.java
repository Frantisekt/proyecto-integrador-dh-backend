package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.request.AdminRequestDTO;
import com.backend.globeonclick.dto.response.AdminResponseDTO;
import java.util.List;

public interface IAdminService {
    AdminResponseDTO createAdmin(AdminRequestDTO adminDTO);
    AdminResponseDTO getAdminById(Long id);
    List<AdminResponseDTO> getAllAdmins();
    AdminResponseDTO updateAdmin(Long id, AdminRequestDTO adminDTO);
    void deleteAdmin(Long id);
    boolean existsByEmail(String email);
}
