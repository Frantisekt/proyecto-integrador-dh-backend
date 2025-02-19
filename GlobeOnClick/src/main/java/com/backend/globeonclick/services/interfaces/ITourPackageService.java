package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.entity.TourPackage;

import java.util.List;
import java.util.Optional;

public interface ITourPackageService {
    TourPackageResponseDTO createTourPackage(TourPackageRequestDTO requestDTO);

    List<TourPackageResponseDTO> getAllTourPackages();

    Optional<TourPackageResponseDTO> getTourPackageById(Long id);

    TourPackageResponseDTO updateTourPackage(Long id, TourPackageRequestDTO requestDTO);

    void deleteTourPackage(Long id);
}
