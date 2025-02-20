package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.request.CategoryRequestDTO;
import com.backend.globeonclick.dto.response.CategoryResponseDTO;
import com.backend.globeonclick.services.interfaces.ICategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Controller", description = "Endpoints para gestión de categorías")
public class CategoryController {

    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(categoryService.createCategory(requestDTO));
    }

    @Operation(summary = "Obtener todas las categorías")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Obtener categoría por id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar categoría")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, requestDTO));
    }

    @Operation(summary = "Eliminar categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener categorías por paquete")
    @GetMapping("/tour-package/{tourPackageId}")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByTourPackageId(
            @PathVariable Long tourPackageId) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesByTourPackageId(tourPackageId);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Asignar media existente a categoría")
    @PostMapping("/{categoryId}/media/{mediaCategoryId}")
    public ResponseEntity<CategoryResponseDTO> addMediaToCategory(
            @PathVariable Long categoryId,
            @PathVariable Long mediaCategoryId) {
        return ResponseEntity.ok(categoryService.addMediaToCategory(categoryId, mediaCategoryId));
    }

    @Operation(summary = "Remover media de categoría")
    @DeleteMapping("/{categoryId}/media/{mediaCategoryId}")
    public ResponseEntity<Void> removeMediaFromCategory(
            @PathVariable Long categoryId,
            @PathVariable Long mediaCategoryId) {
        categoryService.removeMediaFromCategory(categoryId, mediaCategoryId);
        return ResponseEntity.noContent().build();
    }
}