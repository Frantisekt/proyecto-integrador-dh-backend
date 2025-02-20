package com.backend.globeonclick.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {
    private Long userId;
    private Long packageId;
    private Long categoryId;
    private Long dateId;
    private String confirmationStatus;
    //private String paymentStatus;
    private String rating;
}