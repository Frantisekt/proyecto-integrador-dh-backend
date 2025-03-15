package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.response.FeatureResponseDTO;
import com.backend.globeonclick.entity.Feature;
import org.springframework.stereotype.Component;

@Component
public class FeatureMapper {

    public FeatureResponseDTO toDTO(Feature feature) {
        if (feature == null) {
            return null;
        }

        FeatureResponseDTO dto = new FeatureResponseDTO();
        dto.setFeatureId(feature.getFeatureId());
        dto.setName(feature.getName());
        dto.setDisplayName(feature.getDisplayName());

        return dto;
    }

    public Feature toEntity(FeatureResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        Feature feature = new Feature();
        feature.setFeatureId(dto.getFeatureId());
        feature.setName(dto.getName());
        feature.setDisplayName(dto.getDisplayName());

        return feature;
    }
}