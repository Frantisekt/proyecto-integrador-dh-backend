package com.backend.globeonclick.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourDateResponseDTO {
    private Long dateId;
    private String title;
    private String startDate;
    private String endDate;
    private String recommendations;
    private boolean state;
    private Integer level;
    private List<ReservationResponseDTO> reservations;
}