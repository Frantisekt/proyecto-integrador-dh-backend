package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.entity.TourPackage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TourPackageMapper {

    public TourPackageResponseDTO toResponseDTO(TourPackage tourPackage) {
        if (tourPackage == null) return null;

        List<TourPackageResponseDTO.CategoryBasicInfo> categoryInfos = tourPackage.getCategories().stream()
                .map(category -> TourPackageResponseDTO.CategoryBasicInfo.builder()
                        .categoryId(category.getCategoryId())
                        .title(category.getTitle())
                        .description(category.getDescription())
                        .price(category.getPrice())
                        .currency(category.getCurrency())
                        .state(category.isState())
                        .build())
                .collect(Collectors.toList());

        return TourPackageResponseDTO.builder()
                .packageId(tourPackage.getPackageId())
                .title(tourPackage.getTitle())
                .description(tourPackage.getDescription())
                .state(tourPackage.isState())
                .categories(categoryInfos)
                .build();
    }

    public List<TourPackageResponseDTO> toResponseDTOList(List<TourPackage> tourPackages) {
        return tourPackages.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TourPackage toEntity(TourPackageRequestDTO requestDTO) {
        return TourPackage.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .state(requestDTO.isState())
                .build();
    }

    public void updateEntity(TourPackage tourPackage, TourPackageRequestDTO requestDTO) {
        tourPackage.setTitle(requestDTO.getTitle());
        tourPackage.setDescription(requestDTO.getDescription());
        tourPackage.setState(requestDTO.isState());
    }
}