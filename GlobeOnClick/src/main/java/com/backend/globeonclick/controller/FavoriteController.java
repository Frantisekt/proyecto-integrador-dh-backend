package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.response.FavoritePackagesResponseDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.services.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites Controller", description = "Endpoints para gestión de paquetes favoritos")
public class FavoriteController {

    private final IUserService userService;

    @Operation(summary = "Añadir paquete a favoritos")
    @PostMapping("/{packageId}")
    public ResponseEntity<Void> addFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long packageId) {

        Long userId = extractUserId(userDetails);
        userService.addFavoritePackage(userId, packageId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar paquete de favoritos")
    @DeleteMapping("/{packageId}")
    public ResponseEntity<Void> removeFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long packageId) {

        Long userId = extractUserId(userDetails);
        userService.removeFavoritePackage(userId, packageId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener todos los paquetes favoritos del usuario")
    @GetMapping
    public ResponseEntity<FavoritePackagesResponseDTO> getUserFavorites(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = extractUserId(userDetails);
        return ResponseEntity.ok(userService.getUserFavoritePackages(userId));
    }

    @Operation(summary = "Obtener paquetes favoritos con paginación")
    @GetMapping("/paged")
    public ResponseEntity<Page<TourPackageResponseDTO>> getUserFavoritesPaginated(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = extractUserId(userDetails);
        return ResponseEntity.ok(userService.getUserFavoritePackagesPaginated(userId, page, size));
    }

    @Operation(summary = "Verificar si un paquete está en favoritos")
    @GetMapping("/check/{packageId}")
    public ResponseEntity<Boolean> checkFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long packageId) {

        Long userId = extractUserId(userDetails);
        return ResponseEntity.ok(userService.isPackageFavorite(userId, packageId));
    }

    private Long extractUserId(UserDetails userDetails) {
        if (userDetails instanceof com.backend.globeonclick.entity.User) {
            return ((com.backend.globeonclick.entity.User) userDetails).getUserId();
        }
        throw new IllegalArgumentException("Usuario no autenticado correctamente");
    }
}