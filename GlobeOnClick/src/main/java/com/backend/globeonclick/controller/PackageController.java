package com.backend.globeonclick.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
@Tag(name = "Package Controller", description = "Endpoints para gestión de paquetes turísticos")
public class PackageController {
    
    @Operation(summary = "Obtener todos los paquetes")
    @GetMapping
    public ResponseEntity<List<PackageResponseDTO>> getAllPackages() {
        // Implementación
    }
    
    @Operation(summary = "Obtener paquete por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PackageResponseDTO> getPackageById(@PathVariable Long id) {
        // Implementación
    }
    
    @Operation(summary = "Crear nuevo paquete")
    @PostMapping
    public ResponseEntity<PackageResponseDTO> createPackage(@RequestBody PackageRequestDTO packageDTO) {
        // Implementación
    }
    
    @Operation(summary = "Actualizar paquete")
    @PutMapping("/{id}")
    public ResponseEntity<PackageResponseDTO> updatePackage(
            @PathVariable Long id, 
            @RequestBody PackageRequestDTO packageDTO) {
        // Implementación
    }
    
    @Operation(summary = "Eliminar paquete")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        // Implementación
    }
} 