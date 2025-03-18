package com.backend.globeonclick.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRangeResponseDTO {
    private Long id;
    private Double minPrice;
    private Double maxPrice;
    private String rangeLabel;
    private Integer packageCount;
    private Set<Long> packageIds; // Lista de IDs de paquetes en este rango
} 