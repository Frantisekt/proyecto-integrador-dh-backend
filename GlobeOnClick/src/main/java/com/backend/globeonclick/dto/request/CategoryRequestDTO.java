package com.backend.globeonclick.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    private List<Long> tourPackageIds;
    private String title;
    private String description;
    private Double price;
    private String currency;
    private String restrictions;
    private boolean state;
    private Double discount;
    private List<Long> mediaCategoryIds;
}