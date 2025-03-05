package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.response.FeatureResponseDTO;
import com.backend.globeonclick.entity.Feature;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FeatureMapper {
    FeatureMapper INSTANCE = Mappers.getMapper(FeatureMapper.class);

    FeatureResponseDTO toDTO(Feature feature);
    Feature toEntity(FeatureResponseDTO featureDTO);
}