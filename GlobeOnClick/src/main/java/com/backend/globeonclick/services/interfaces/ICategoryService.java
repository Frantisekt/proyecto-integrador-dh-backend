package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.request.CategoryRequestDTO;
import com.backend.globeonclick.dto.response.CategoryResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    List<CategoryResponseDTO> getAllCategories();

    Optional<CategoryResponseDTO> getCategoryById(Long id);

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO);

    void deleteCategory(Long id);

    List<CategoryResponseDTO> getCategoriesByTourPackageId(Long tourPackageId);

    CategoryResponseDTO addMediaToCategory(Long categoryId, Long mediaCategoryId);

    void removeMediaFromCategory(Long categoryId, Long mediaCategoryId);
}