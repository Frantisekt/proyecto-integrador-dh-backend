package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.request.AdminRequestDTO;
import com.backend.globeonclick.dto.response.AdminResponseDTO;
import com.backend.globeonclick.services.interfaces.IAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "Endpoints para gestión de administradores")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final IAdminService adminService;

    @Operation(summary = "Obtener todos los administradores")
    @GetMapping
    public ResponseEntity<List<AdminResponseDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @Operation(summary = "Obtener administrador por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @Operation(summary = "Crear nuevo administrador")
    @PostMapping
    public ResponseEntity<AdminResponseDTO> createAdmin(@RequestBody AdminRequestDTO adminDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.createAdmin(adminDTO));
    }

    @Operation(summary = "Actualizar administrador")
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(
            @PathVariable Long id,
            @RequestBody AdminRequestDTO adminDTO) {
        return ResponseEntity.ok(adminService.updateAdmin(id, adminDTO));
    }

    @Operation(summary = "Eliminar administrador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}