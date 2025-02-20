package com.backend.globeonclick.dto.response;

import lombok.*;

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
    private Long categoryId;
    private String categoryTitle;
    private Long dateId;
    private String dateTitle;
    private String confirmationStatus;
    //private String paymentStatus;
    private String rating;
}