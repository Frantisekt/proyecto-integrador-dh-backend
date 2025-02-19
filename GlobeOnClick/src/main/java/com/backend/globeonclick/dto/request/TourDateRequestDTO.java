package com.backend.globeonclick.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDateRequestDTO {
    private String title;
    private String startDate;
    private String endDate;
    private String recommendations;
    private boolean state;
    private Integer level;
}