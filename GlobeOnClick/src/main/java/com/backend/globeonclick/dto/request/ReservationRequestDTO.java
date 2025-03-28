package com.backend.globeonclick.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {
    @NotNull(message = "El ID del usuario es requerido")
    private Long userId;
    
    @NotNull(message = "El ID del paquete es requerido")
    private Long packageId;
    
    @NotNull(message = "El número de adultos es requerido")
    @Min(1)
    private Integer numberOfAdults;
    
    @NotNull
    @Min(0)
    private Integer numberOfChildren;
    
    @NotNull
    @Min(0)
    private Integer numberOfInfants;
    
    private String confirmationStatus;
    private String rating;
}