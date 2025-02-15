package com.backend.globeonclick.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
@Tag(name = "Media Controller", description = "Endpoints para gestión de archivos multimedia")
public class MediaController {
    
    @Operation(summary = "Subir archivo multimedia")
    @PostMapping("/upload")
    public ResponseEntity<MediaResponseDTO> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("categoryId") Long categoryId) {
        // Implementación
    }
    
    @Operation(summary = "Obtener multimedia por categoría")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MediaCategoryResponseDTO>> getMediaByCategory(@PathVariable Long categoryId) {
        // Implementación
    }
    
    @Operation(summary = "Eliminar archivo multimedia")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        // Implementación
    }
} 