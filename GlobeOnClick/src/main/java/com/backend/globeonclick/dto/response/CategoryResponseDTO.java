package com.backend.globeonclick.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long categoryId;
    private String title;
    private String description;
    private Double price;
    private String currency;
    private String restrictions;
    private boolean state;
    private Double discount;
    private List<PackageBasicInfo> tourPackages;
    private List<MediaCategoryResponseDTO> mediaCategories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageBasicInfo {
        private Long packageId;
        private String title;
    }
}

