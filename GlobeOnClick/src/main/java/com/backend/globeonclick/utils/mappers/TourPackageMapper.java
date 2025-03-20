package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.MediaPackageResponseDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.entity.*;
import com.backend.globeonclick.repository.FeatureRepository;
import com.backend.globeonclick.repository.IMediaPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

        // Procesamos solo las colecciones necesarias y con límites claros
        List<TourPackageResponseDTO.CategoryBasicInfo> categoryInfos = new ArrayList<>(tourPackage.getCategories().size());
        List<FeatureName> featureNames = new ArrayList<>(tourPackage.getFeatures().size());
        List<MediaPackageResponseDTO> mediaPackageDTOs = new ArrayList<>(
                Math.min(tourPackage.getMediaPackages().size(), 20));

        // Procesamos categorías
        for (Category category : tourPackage.getCategories()) {
            categoryInfos.add(TourPackageResponseDTO.CategoryBasicInfo.builder()
                    .categoryId(category.getCategoryId())
                    .title(category.getTitle())
                    .description(category.getDescription())
                    .price(category.getPrice())
                    .currency(category.getCurrency())
                    .state(category.isState())
                    .build());
        }

        // Procesamos features
        for (Feature feature : tourPackage.getFeatures()) {
            if (feature.getName() != null) {
                featureNames.add(FeatureName.valueOf(feature.getName()));
            }
        }

        // Procesamos mediaPackages (ya limitados a 20 en la consulta)
        for (MediaPackage mp : tourPackage.getMediaPackages()) {
            mediaPackageDTOs.add(mediaPackageMapper.toResponseDTO(mp));
        }

        return TourPackageResponseDTO.builder()
                .packageId(tourPackage.getPackageId())
                .title(tourPackage.getTitle())
                .description(tourPackage.getDescription())
                .state(tourPackage.isState())
                .categories(categoryInfos)
                .mediaPackages(mediaPackageDTOs)
                .features(featureNames)
                .start_date(tourPackage.getStart_date())
                .end_date(tourPackage.getEnd_date())
                .price(tourPackage.getPrice())
                .build();
    }

    public TourPackage toEntity(TourPackageRequestDTO requestDTO) {

        if (checkDate(requestDTO.getStart_date(), requestDTO.getEnd_date())) {

            TourPackage tourPackage = TourPackage.builder()
                    .title(requestDTO.getTitle())
                    .description(requestDTO.getDescription())
                    .state(requestDTO.isState())
                    .mediaPackages(new ArrayList<>())
                    .features(new ArrayList<>())
                    .start_date(requestDTO.getStart_date())
                    .end_date(requestDTO.getEnd_date())
                    .end_date(requestDTO.getEnd_date())
                    .price(requestDTO.getPrice())
                    .build();

            if (requestDTO.getFeatureIds() != null && !requestDTO.getFeatureIds().isEmpty()) {
                List<Feature> features = featureRepository.findAllById(requestDTO.getFeatureIds());
                features.forEach(tourPackage::addFeature);
            }

            return tourPackage;
        } else {
            System.out.println("La fecha de inicio no puede ser mayor a la de final");
            return null;
        }
    }

    public void updateEntity(TourPackage tourPackage, TourPackageRequestDTO requestDTO) {

        if (checkDate(requestDTO.getStart_date(), requestDTO.getEnd_date())) {
            tourPackage.setTitle(requestDTO.getTitle());
            tourPackage.setDescription(requestDTO.getDescription());
            tourPackage.setState(requestDTO.isState());
            tourPackage.setStart_date(requestDTO.getStart_date());
            tourPackage.setEnd_date(requestDTO.getEnd_date());
            tourPackage.setPrice(requestDTO.getPrice());

            if (requestDTO.getMediaPackageIds() != null && !requestDTO.getMediaPackageIds().isEmpty()) {
                List<MediaPackage> mediaPackages = mediaPackageRepository.findAllById(requestDTO.getMediaPackageIds());
                mediaPackages.forEach(tourPackage::addMediaPackage);
            }

            tourPackage.getFeatures().clear();
            if (requestDTO.getFeatureIds() != null && !requestDTO.getFeatureIds().isEmpty()) {
                List<Feature> features = featureRepository.findAllById(requestDTO.getFeatureIds());
                features.forEach(tourPackage::addFeature);
            }
        } else {
            System.out.println("La fecha de inicio no puede ser mayor a la de final");
        }

    }

    private boolean checkDate (LocalDate start_date, LocalDate end_date) {
        return start_date.isBefore(end_date);
    }
}