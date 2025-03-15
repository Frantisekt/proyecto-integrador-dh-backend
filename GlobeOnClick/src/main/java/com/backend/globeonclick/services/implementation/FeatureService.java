package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.response.FeatureResponseDTO;
import com.backend.globeonclick.entity.Feature;

import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.repository.FeatureRepository;
import com.backend.globeonclick.repository.ITourPackageRepository;
import com.backend.globeonclick.utils.mappers.FeatureMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final FeatureRepository featureRepository;
    private final ITourPackageRepository tourPackageRepository;
    private final FeatureMapper featureMapper;

    // Nuevo método para crear feature con nombre y displayName
    @Transactional
    public FeatureResponseDTO createFeature(String name, String displayName) {
        if (featureRepository.existsByName(name)) {
            throw new IllegalArgumentException("La característica ya existe: " + name);
        }

        Feature feature = Feature.builder()
                .name(name)
                .displayName(displayName)
                .build();

        return featureMapper.toDTO(featureRepository.save(feature));
    }

    @Transactional
    public FeatureResponseDTO createFeature(String featureName) {
        return createFeature(featureName, featureName);
    }

    @Transactional(readOnly = true)
    public List<FeatureResponseDTO> getAllFeatures() {
        return featureRepository.findAll().stream()
                .map(featureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFeatureToPackage(Long packageId, String featureName) {
        TourPackage tourPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado con ID: " + packageId));

        Feature feature = featureRepository.findByName(featureName)
                .orElseGet(() -> {
                    FeatureResponseDTO featureDTO = createFeature(featureName);
                    return featureRepository.findByName(featureDTO.getName())
                            .orElseThrow(() -> new EntityNotFoundException("Error al crear la característica: " + featureName));
                });

        tourPackage.addFeature(feature);
        tourPackageRepository.save(tourPackage);
    }

    @Transactional
    public void removeFeatureFromPackage(Long packageId, String featureName) {
        TourPackage tourPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado con ID: " + packageId));

        Feature feature = featureRepository.findByName(featureName)
                .orElseThrow(() -> new EntityNotFoundException("Característica no encontrada: " + featureName));

        tourPackage.removeFeature(feature);
        tourPackageRepository.save(tourPackage);
    }

    @Transactional(readOnly = true)
    public List<FeatureResponseDTO> getFeaturesForPackage(Long packageId) {
        TourPackage tourPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new EntityNotFoundException("Paquete no encontrado con ID: " + packageId));

        return tourPackage.getFeatures().stream()
                .map(featureMapper::toDTO)
                .collect(Collectors.toList());
    }
}