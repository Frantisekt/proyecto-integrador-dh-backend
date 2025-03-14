package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.services.interfaces.ITourPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tourPackages")
@Tag(name = "Package Controller", description = "Endpoints para gestión de paquetes turísticos")
public class TourPackageController {

    private final ITourPackageService tourPackageService;

    @Autowired
    public TourPackageController(ITourPackageService tourPackageService) {
        this.tourPackageService = tourPackageService;
    }

    @Operation(summary = "Crear nuevo paquete")
    @PostMapping
    public ResponseEntity<TourPackageResponseDTO> createPackage(@RequestBody TourPackageRequestDTO packageDTO) {
        TourPackageResponseDTO createdPackage = tourPackageService.createTourPackage(packageDTO);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los paquetes")
    @GetMapping
    public ResponseEntity<List<TourPackageResponseDTO>> getAllPackages() {
        List<TourPackageResponseDTO> packages = tourPackageService.getAllTourPackages();
        return ResponseEntity.ok(packages);
    }

    @Operation(summary = "Obtener paquete por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TourPackageResponseDTO> getPackageById(@PathVariable Long id) {
        return tourPackageService.getTourPackageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar paquete")
    @PutMapping("/{id}")
    public ResponseEntity<TourPackageResponseDTO> updatePackage(
            @PathVariable Long id,
            @RequestBody TourPackageRequestDTO packageDTO) {
        TourPackageResponseDTO updatedPackage = tourPackageService.updateTourPackage(id, packageDTO);
        return updatedPackage != null ?
                ResponseEntity.ok(updatedPackage) :
                ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar paquete")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        tourPackageService.deleteTourPackage(id);
        return ResponseEntity.noContent().build();
    }
}