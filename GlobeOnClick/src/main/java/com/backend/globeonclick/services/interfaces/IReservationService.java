package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.request.ReservationRequestDTO;
import com.backend.globeonclick.dto.response.ReservationResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IReservationService {
    ReservationResponseDTO createReservation(ReservationRequestDTO requestDTO);

    List<ReservationResponseDTO> getAllReservations();

    Optional<ReservationResponseDTO> getReservationById(Long id);

    ReservationResponseDTO updateReservation(Long id, ReservationRequestDTO requestDTO);

    void deleteReservation(Long id);

    List<ReservationResponseDTO> getReservationsByUserId(Long userId);

    ReservationResponseDTO updateConfirmationStatus(Long id, String status);

    //ReservationResponseDTO updatePaymentStatus(Long id, String status);

    ReservationResponseDTO updateRating(Long id, String rating);
}