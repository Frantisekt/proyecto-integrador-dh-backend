package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.CategoryRequestDTO;
import com.backend.globeonclick.dto.response.CategoryResponseDTO;
import com.backend.globeonclick.dto.response.MediaCategoryResponseDTO;
import com.backend.globeonclick.entity.Category;
import com.backend.globeonclick.entity.MediaCategory;
import com.backend.globeonclick.repository.IMediaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final MediaCategoryMapper mediaCategoryMapper;
    private final IMediaCategoryRepository mediaCategoryRepository;

    public CategoryResponseDTO toResponseDTO(Category category) {
        if (category == null) return null;

        Set<MediaCategoryResponseDTO> uniqueMediaCategories = category.getMediaCategories().stream()
                .map(mediaCategoryMapper::toResponseDTO)
                .sorted(Comparator.comparing(MediaCategoryResponseDTO::getMediaCategoryId))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return CategoryResponseDTO.builder()
                .categoryId(category.getCategoryId())
                .title(category.getTitle())
                .description(category.getDescription())
                .price(category.getPrice())
                .currency(category.getCurrency())
                .restrictions(category.getRestrictions())
                .state(category.isState())
                .discount(category.getDiscount())
                .tourPackages(category.getTourPackages() != null
                        ? category.getTourPackages().stream()
                        .map(pkg -> CategoryResponseDTO.PackageBasicInfo.builder()
                                .packageId(pkg.getPackageId())
                                .title(pkg.getTitle())
                                .build())
                        .collect(Collectors.toList())
                        : List.of())
                .mediaCategories(new ArrayList<>(uniqueMediaCategories))
                .build();
    }

    public Category toEntity(CategoryRequestDTO requestDTO) {
        Category category = Category.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .price(requestDTO.getPrice())
                .currency(requestDTO.getCurrency())
                .restrictions(requestDTO.getRestrictions())
                .state(requestDTO.isState())
                .discount(requestDTO.getDiscount())
                .tourPackages(new ArrayList<>())
                .mediaCategories(new ArrayList<>())
                .build();

        if (requestDTO.getMediaCategoryIds() != null && !requestDTO.getMediaCategoryIds().isEmpty()) {
            List<MediaCategory> mediaCategories = mediaCategoryRepository.findAllById(requestDTO.getMediaCategoryIds());
            mediaCategories.forEach(category::addMediaCategory);
        }

        return category;
    }

    public void updateEntity(Category category, CategoryRequestDTO requestDTO) {
        category.setTitle(requestDTO.getTitle());
        category.setDescription(requestDTO.getDescription());
        category.setPrice(requestDTO.getPrice());
        category.setCurrency(requestDTO.getCurrency());
        category.setRestrictions(requestDTO.getRestrictions());
        category.setState(requestDTO.isState());
        category.setDiscount(requestDTO.getDiscount());

        //category.getMediaCategories().clear();
        if (requestDTO.getMediaCategoryIds() != null && !requestDTO.getMediaCategoryIds().isEmpty()) {
            List<MediaCategory> mediaCategories = mediaCategoryRepository.findAllById(requestDTO.getMediaCategoryIds());
            mediaCategories.forEach(category::addMediaCategory);
        }
    }
}