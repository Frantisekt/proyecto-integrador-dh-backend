package com.backend.globeonclick.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourPackageRequestDTO {
    private String title;
    private String description;
    private boolean state;
}