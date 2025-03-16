package com.backend.globeonclick.dto.response;

import com.backend.globeonclick.entity.FeatureName;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourPackageResponseDTO {
    private Long packageId;
    private String title;
    private String description;
    private boolean state;
    private LocalDate start_date;
    private LocalDate end_date;
    private Double price;
    private List<CategoryBasicInfo> categories;
    private List<MediaPackageResponseDTO> mediaPackages;
    private List<FeatureName> features;
    private Boolean isFavorite = false;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryBasicInfo {
        private Long categoryId;
        private String title;
        private String description;
        private String currency;
        private boolean state;
        private Double price;
    }
}