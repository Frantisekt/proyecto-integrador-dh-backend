package com.backend.globeonclick.validator;

import com.backend.globeonclick.dto.request.ReservationRequestDTO;
import com.backend.globeonclick.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationValidator {
    public void validateReservation(ReservationRequestDTO dto) {
        List<String> errors = new ArrayList<>();
        
        if (dto.getUserId() == null) {
            errors.add("El ID del usuario es requerido");
        }
        
        if (dto.getPackageId() == null) {
            errors.add("El ID del paquete es requerido");
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(", ", errors));
        }
    }
} 