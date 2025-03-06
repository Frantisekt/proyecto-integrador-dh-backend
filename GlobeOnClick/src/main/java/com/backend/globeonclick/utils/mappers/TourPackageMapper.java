package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.MediaPackageResponseDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.entity.Feature;
import com.backend.globeonclick.entity.FeatureName;
import com.backend.globeonclick.entity.MediaPackage;
import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.repository.FeatureRepository;
import com.backend.globeonclick.repository.IMediaPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TourPackageMapper {

    private final MediaPackageMapper mediaPackageMapper;
    private final IMediaPackageRepository mediaPackageRepository;
    private final FeatureRepository featureRepository;

    public TourPackageResponseDTO toResponseDTO(TourPackage tourPackage) {
        if (tourPackage == null) return null;

        Set<MediaPackageResponseDTO> uniqueMediaPackages = tourPackage.getMediaPackages().stream()
                .map(mediaPackageMapper::toResponseDTO)
                .sorted(Comparator.comparing(MediaPackageResponseDTO::getMediaPackageId))
                .collect(Collectors.toCollection(LinkedHashSet::new));

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

        List<FeatureName> featureNames = tourPackage.getFeatures().stream()
                .map(Feature::getName)
                .collect(Collectors.toList());

        return TourPackageResponseDTO.builder()
                .packageId(tourPackage.getPackageId())
                .title(tourPackage.getTitle())
                .description(tourPackage.getDescription())
                .state(tourPackage.isState())
                .categories(categoryInfos)
                .mediaPackages(new ArrayList<>(uniqueMediaPackages))
                .features(featureNames)
                .build();
    }

    public List<TourPackageResponseDTO> toResponseDTOList(List<TourPackage> tourPackages) {
        return tourPackages.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TourPackage toEntity(TourPackageRequestDTO requestDTO) {
        TourPackage tourPackage = TourPackage.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .state(requestDTO.isState())
                .mediaPackages(new ArrayList<>())
                .features(new ArrayList<>())
                .build();

        if (requestDTO.getFeatureIds() != null && !requestDTO.getFeatureIds().isEmpty()) {
            List<Feature> features = featureRepository.findAllById(requestDTO.getFeatureIds());
            features.forEach(tourPackage::addFeature);
        }

        return tourPackage;
    }

    public void updateEntity(TourPackage tourPackage, TourPackageRequestDTO requestDTO) {
        tourPackage.setTitle(requestDTO.getTitle());
        tourPackage.setDescription(requestDTO.getDescription());
        tourPackage.setState(requestDTO.isState());

        if (requestDTO.getMediaPackageIds() != null && !requestDTO.getMediaPackageIds().isEmpty()) {
            List<MediaPackage> mediaPackages = mediaPackageRepository.findAllById(requestDTO.getMediaPackageIds());
            mediaPackages.forEach(tourPackage::addMediaPackage);
        }

        tourPackage.getFeatures().clear();
        if (requestDTO.getFeatureIds() != null && !requestDTO.getFeatureIds().isEmpty()) {
            List<Feature> features = featureRepository.findAllById(requestDTO.getFeatureIds());
            features.forEach(tourPackage::addFeature);
        }
    }
}