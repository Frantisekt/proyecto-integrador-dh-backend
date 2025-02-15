package com.backend.globeonclick.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Controller", description = "Endpoints para gestión de categorías")
public class CategoryController {
    
    @Operation(summary = "Obtener todas las categorías")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        // Implementación
    }
    
    @Operation(summary = "Obtener categorías por paquete")
    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByPackage(@PathVariable Long packageId) {
        // Implementación
    }
    
    @Operation(summary = "Crear nueva categoría")
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryDTO) {
        // Implementación
    }
    
    @Operation(summary = "Actualizar categoría")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id, 
            @RequestBody CategoryRequestDTO categoryDTO) {
        // Implementación
    }
} 