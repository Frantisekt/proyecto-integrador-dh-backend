package com.backend.globeonclick.dto.response;

import com.backend.globeonclick.entity.FeatureName;
import lombok.*;
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
    private List<CategoryBasicInfo> categories;
    private List<MediaPackageResponseDTO> mediaPackages;
    private List<FeatureName> features;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryBasicInfo {
        private Long categoryId;
        private String title;
        private String description;
        private Double price;
        private String currency;
        private boolean state;
    }
}
