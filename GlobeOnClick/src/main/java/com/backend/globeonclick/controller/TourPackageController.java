package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.services.interfaces.ITourPackageService;
import com.backend.globeonclick.services.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tourPackages")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "Package Controller", description = "Endpoints para gestión de paquetes turísticos")
public class TourPackageController {

    private final ITourPackageService tourPackageService;
    private final IUserService userService;

    @Operation(summary = "Obtener todos los paquetes (con paginación)")
    @GetMapping("/paged")
    public ResponseEntity<Page<TourPackageResponseDTO>> getAllPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TourPackageResponseDTO> packages = tourPackageService.getAllTourPackagesPaginated(page, size);

        // Agregar información de favoritos si hay un usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof com.backend.globeonclick.entity.User) {
            Long userId = ((com.backend.globeonclick.entity.User) auth.getPrincipal()).getUserId();
            packages.getContent().forEach(packageDTO ->
                    packageDTO.setIsFavorite(userService.isPackageFavorite(userId, packageDTO.getPackageId())));
        }

        return ResponseEntity.ok(packages);
    }

    @Operation(summary = "Crear nuevo paquete")
    @PostMapping
    public ResponseEntity<TourPackageResponseDTO> createPackage(@RequestBody TourPackageRequestDTO packageDTO) {
        TourPackageResponseDTO createdPackage = tourPackageService.createTourPackage(packageDTO);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener paquete por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TourPackageResponseDTO> getPackageById(@PathVariable Long id) {
        return tourPackageService.getTourPackageById(id)
                .map(packageDTO -> {
                    // Agregar información de favoritos si hay un usuario autenticado
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null && auth.getPrincipal() instanceof com.backend.globeonclick.entity.User) {
                        Long userId = ((com.backend.globeonclick.entity.User) auth.getPrincipal()).getUserId();
                        packageDTO.setIsFavorite(userService.isPackageFavorite(userId, packageDTO.getPackageId()));
                    }
                    return ResponseEntity.ok(packageDTO);
                })
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

    @Operation(summary = "Asignar media existente a paquete")
    @PostMapping("/{packageId}/media/{mediaPackageId}")
    public ResponseEntity<TourPackageResponseDTO> addMediaToPackage(
            @PathVariable Long packageId,
            @PathVariable Long mediaPackageId) {
        return ResponseEntity.ok(tourPackageService.addMediaToTourPackage(packageId, mediaPackageId));
    }

    @Operation(summary = "Remover media de paquete")
    @DeleteMapping("/{packageId}/media/{mediaPackageId}")
    public ResponseEntity<Void> removeMediaFromPackage(
            @PathVariable Long packageId,
            @PathVariable Long mediaPackageId) {
        tourPackageService.removeMediaFromTourPackage(packageId, mediaPackageId);
        return ResponseEntity.noContent().build();
    }
}