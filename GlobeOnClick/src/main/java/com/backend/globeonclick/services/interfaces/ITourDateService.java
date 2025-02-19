package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.request.TourDateRequestDTO;
import com.backend.globeonclick.dto.response.TourDateResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ITourDateService {
    TourDateResponseDTO createDate(TourDateRequestDTO requestDTO);

    List<TourDateResponseDTO> getAllDates();

    Optional<TourDateResponseDTO> getDateById(Long id);

    TourDateResponseDTO updateDate(Long id, TourDateRequestDTO requestDTO);

    void deleteDate(Long id);

    TourDateResponseDTO updateDateState(Long id, boolean state);

    List<TourDateResponseDTO> getAvailableDates();
}