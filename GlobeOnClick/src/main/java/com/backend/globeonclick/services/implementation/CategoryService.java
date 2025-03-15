package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.request.CategoryRequestDTO;
import com.backend.globeonclick.dto.response.CategoryResponseDTO;
import com.backend.globeonclick.dto.response.MediaResponseDTO;
import com.backend.globeonclick.entity.Category;
import com.backend.globeonclick.entity.Media;
import com.backend.globeonclick.entity.MediaCategory;
import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.repository.ICategoryRepository;
import com.backend.globeonclick.repository.IMediaCategoryRepository;
import com.backend.globeonclick.repository.ITourPackageRepository;
import com.backend.globeonclick.services.interfaces.ICategoryService;
import com.backend.globeonclick.services.interfaces.IMediaService;
import com.backend.globeonclick.utils.mappers.CategoryMapper;
import com.backend.globeonclick.utils.mappers.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final ITourPackageRepository tourPackageRepository;
    private final CategoryMapper categoryMapper;
    private final IMediaCategoryRepository mediaCategoryRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        Category category = categoryMapper.toEntity(requestDTO);

        category = categoryRepository.save(category);

        if (requestDTO.getTourPackageIds() != null && !requestDTO.getTourPackageIds().isEmpty()) {
            List<TourPackage> packages = tourPackageRepository.findAllById(requestDTO.getTourPackageIds());
            category.setTourPackages(packages);
        }

        if (requestDTO.getMediaCategoryIds() != null && !requestDTO.getMediaCategoryIds().isEmpty()) {
            List<MediaCategory> mediaCategories = mediaCategoryRepository.findAllById(requestDTO.getMediaCategoryIds());
            for (MediaCategory mediaCategory : mediaCategories) {
                category.addMediaCategory(mediaCategory);
                mediaCategory.addCategory(category);
                mediaCategoryRepository.save(mediaCategory);
            }
        }

        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryResponseDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponseDTO);
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        categoryMapper.updateEntity(category, requestDTO);
        category.setTourPackages(tourPackageRepository.findAllById(requestDTO.getTourPackageIds()));

        category.getMediaCategories().clear();
        if (requestDTO.getMediaCategoryIds() != null && !requestDTO.getMediaCategoryIds().isEmpty()) {
            List<MediaCategory> mediaCategories = mediaCategoryRepository.findAllById(requestDTO.getMediaCategoryIds());
            mediaCategories.forEach(category::addMediaCategory);
        }

        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Limpiar la relación con los paquetes turísticos
        category.getTourPackages().forEach(tourPackage -> 
            tourPackage.getCategories().remove(category)
        );
        category.getTourPackages().clear();

        // Limpiar la relación con las medias
        category.getMediaCategories().forEach(mediaCategory -> 
            mediaCategory.getCategories().remove(category)
        );
        category.getMediaCategories().clear();

        // Guardar los cambios en las relaciones
        categoryRepository.save(category);

        // Finalmente eliminar la categoría
        categoryRepository.delete(category);
    }


    @Override
    public List<CategoryResponseDTO> getCategoriesByTourPackageId(Long tourPackageId) {
        List<Category> categories = categoryRepository.findByTourPackagesPackageId(tourPackageId);
        return categories.stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO addMediaToCategory(Long categoryId, Long mediaCategoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        MediaCategory mediaCategory = mediaCategoryRepository.findById(mediaCategoryId)
                .orElseThrow(() -> new RuntimeException("MediaCategory not found with id: " + mediaCategoryId));

        category.addMediaCategory(mediaCategory);
        mediaCategory.addCategory(category);

        mediaCategoryRepository.save(mediaCategory);
        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Override
    public void removeMediaFromCategory(Long categoryId, Long mediaCategoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        MediaCategory mediaCategory = mediaCategoryRepository.findById(mediaCategoryId)
                .orElseThrow(() -> new RuntimeException("MediaCategory not found with id: " + mediaCategoryId));

        category.removeMediaCategory(mediaCategory);
        categoryRepository.save(category);
    }
}
