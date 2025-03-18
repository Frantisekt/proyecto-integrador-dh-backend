package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.response.PriceRangeResponseDTO;
import com.backend.globeonclick.entity.PriceRange;
import org.springframework.stereotype.Component;

@Component
public class PriceRangeMapper {
    
    public PriceRangeResponseDTO toDTO(PriceRange priceRange) {
        if (priceRange == null) {
            return null;
        }

        return PriceRangeResponseDTO.builder()
                .id(priceRange.getId())
                .minPrice(priceRange.getMinPrice())
                .maxPrice(priceRange.getMaxPrice())
                .rangeLabel(priceRange.getRangeLabel())
                .packageCount(priceRange.getPackageCount())
                .packageIds(priceRange.getPackageIds())
                .build();
    }
} 