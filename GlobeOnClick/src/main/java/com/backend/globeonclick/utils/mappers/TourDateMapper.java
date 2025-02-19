package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.TourDateRequestDTO;
import com.backend.globeonclick.dto.response.TourDateResponseDTO;
import com.backend.globeonclick.entity.TourDate;
import org.springframework.stereotype.Component;

@Component
public class TourDateMapper {
    public TourDateResponseDTO toResponseDTO(TourDate tourDate) {
        if (tourDate == null) return null;

        return TourDateResponseDTO.builder()
                .dateId(tourDate.getDateId())
                .title(tourDate.getTitle())
                .startDate(tourDate.getStartDate())
                .endDate(tourDate.getEndDate())
                .recommendations(tourDate.getRecommendations())
                .state(tourDate.isState())
                .level(tourDate.getLevel())
                .build();
    }

    public TourDate toEntity(TourDateRequestDTO requestDTO) {
        return TourDate.builder()
                .title(requestDTO.getTitle())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .recommendations(requestDTO.getRecommendations())
                .state(requestDTO.isState())
                .level(requestDTO.getLevel())
                .build();
    }

    public void updateEntity(TourDate tourDate, TourDateRequestDTO requestDTO) {
        tourDate.setTitle(requestDTO.getTitle());
        tourDate.setStartDate(requestDTO.getStartDate());
        tourDate.setEndDate(requestDTO.getEndDate());
        tourDate.setRecommendations(requestDTO.getRecommendations());
        tourDate.setState(requestDTO.isState());
        tourDate.setLevel(requestDTO.getLevel());
    }
}