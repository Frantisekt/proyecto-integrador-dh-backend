package com.backend.globeonclick.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoritePackagesResponseDTO {
    private List<TourPackageResponseDTO> favoritePackages;
}