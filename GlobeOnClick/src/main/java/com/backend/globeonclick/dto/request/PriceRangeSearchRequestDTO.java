package com.backend.globeonclick.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRangeSearchRequestDTO {
    @NotNull(message = "El precio mínimo no puede ser nulo")
    @Min(value = 0, message = "El precio mínimo debe ser mayor o igual a 0")
    private Double minPrice;

    @NotNull(message = "El precio máximo no puede ser nulo")
    @Min(value = 0, message = "El precio máximo debe ser mayor o igual a 0")
    private Double maxPrice;

    @Builder.Default
    @Min(value = 0, message = "El número de página debe ser mayor o igual a 0")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "El tamaño de página debe ser mayor a 0")
    private Integer size = 10;
}