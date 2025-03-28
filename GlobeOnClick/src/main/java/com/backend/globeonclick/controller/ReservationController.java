package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.request.ReservationRequestDTO;
import com.backend.globeonclick.dto.response.ReservationResponseDTO;
import com.backend.globeonclick.services.interfaces.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation Controller", description = "Endpoints para gestión de reservaciones")
public class ReservationController {

    private final IReservationService reservationService;

    @Operation(summary = "Crear nueva reservación")
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationRequestDTO requestDTO) {
        ReservationResponseDTO createdReservation = reservationService.createReservation(requestDTO);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todas las reservaciones")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @Operation(summary = "Obtener reservación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener reservaciones por ID de usuario")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }

    @Operation(summary = "Actualizar estado de confirmación")
    @PutMapping("/{id}/confirmation-status")
    public ResponseEntity<ReservationResponseDTO> updateConfirmationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(reservationService.updateConfirmationStatus(id, status));
    }

    @Operation(summary = "Actualizar calificación")
    @PutMapping("/{id}/rating")
    public ResponseEntity<ReservationResponseDTO> updateRating(
            @PathVariable Long id,
            @RequestParam String rating) {
        return ResponseEntity.ok(reservationService.updateRating(id, rating));
    }

    @Operation(summary = "Actualizar reservación")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> updateReservation(
            @PathVariable Long id,
            @RequestBody ReservationRequestDTO requestDTO) {
        return ResponseEntity.ok(reservationService.updateReservation(id, requestDTO));
    }

    @Operation(summary = "Eliminar reservación")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}