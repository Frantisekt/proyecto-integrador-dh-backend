package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.CategoryResponseDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.entity.TourPackage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ITourPackageService {
    TourPackageResponseDTO createTourPackage(TourPackageRequestDTO requestDTO);

    //List<TourPackageResponseDTO> getAllTourPackages();
    Page<TourPackageResponseDTO> getAllTourPackagesPaginated(int page, int size);

    Optional<TourPackageResponseDTO> getTourPackageById(Long id);

    TourPackageResponseDTO updateTourPackage(Long id, TourPackageRequestDTO requestDTO);

    void deleteTourPackage(Long id);

    TourPackageResponseDTO addMediaToTourPackage(Long packageId, Long mediaPackageId);

    void removeMediaFromTourPackage(Long packageId, Long mediaPackageId);
}
