package com.backend.globeonclick.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourPackageRequestDTO {
    private String title;
    private String description;
    private boolean state;
    private List<Long> mediaPackageIds;
    private List<Long> featureIds;

}