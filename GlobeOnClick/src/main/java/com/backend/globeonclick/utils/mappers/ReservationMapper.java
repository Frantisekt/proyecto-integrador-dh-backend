package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.ReservationRequestDTO;
import com.backend.globeonclick.dto.response.ReservationResponseDTO;
import com.backend.globeonclick.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public ReservationResponseDTO toResponseDTO(Reservation reservation) {
        if (reservation == null) return null;

        return ReservationResponseDTO.builder()
                .reservationId(reservation.getReservationId())
                .userId(reservation.getUser().getUserId())
                .userName(reservation.getUser().getName())
                .packageId(reservation.getTourPackage().getPackageId())
                .packageTitle(reservation.getTourPackage().getTitle())
                .categoryId(reservation.getCategory().getCategoryId())
                .categoryTitle(reservation.getCategory().getTitle())
                .dateId(reservation.getTourDate().getDateId())
                .dateTitle(reservation.getTourDate().getTitle())
                .confirmationStatus(reservation.getConfirmationStatus())
                //.paymentStatus(reservation.getPaymentStatus())
                .rating(reservation.getRating())
                .build();
    }

    public Reservation toEntity(ReservationRequestDTO requestDTO) {
        return Reservation.builder()
                .confirmationStatus(requestDTO.getConfirmationStatus())
                //.paymentStatus(requestDTO.getPaymentStatus())
                .rating(requestDTO.getRating())
                .build();
    }

    public void updateEntity(Reservation reservation, ReservationRequestDTO requestDTO) {
        reservation.setConfirmationStatus(requestDTO.getConfirmationStatus());
        //reservation.setPaymentStatus(requestDTO.getPaymentStatus());
        reservation.setRating(requestDTO.getRating());
    }
}