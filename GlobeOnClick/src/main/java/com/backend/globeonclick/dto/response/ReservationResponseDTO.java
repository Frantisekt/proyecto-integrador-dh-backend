package com.backend.globeonclick.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    private Long reservationId;
    private Long userId;
    private String userName;
    private Long packageId;
    private String packageTitle;
    private Integer numberOfAdults;
    private Integer numberOfChildren;
    private Integer numberOfInfants;
    private Double totalAmount;
    private String confirmationStatus;
    private String rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}