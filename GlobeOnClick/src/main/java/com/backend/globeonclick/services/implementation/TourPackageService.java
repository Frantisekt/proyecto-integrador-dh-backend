package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.repository.ITourPackageRepository;
import com.backend.globeonclick.services.interfaces.ITourPackageService;
import com.backend.globeonclick.utils.mappers.TourPackageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TourPackageService implements ITourPackageService {

    private final ITourPackageRepository tourPackageRepository;
    private final TourPackageMapper tourPackageMapper;

    @Override
    public TourPackageResponseDTO createTourPackage(TourPackageRequestDTO requestDTO) {
        TourPackage tourPackage = tourPackageMapper.toEntity(requestDTO);
        TourPackage savedTourPackage = tourPackageRepository.save(tourPackage);
        return tourPackageMapper.toResponseDTO(savedTourPackage);
    }

    @Override
    public List<TourPackageResponseDTO> getAllTourPackages() {
        List<TourPackage> tourPackages = tourPackageRepository.findAll();
        return tourPackageMapper.toResponseDTOList(tourPackages);
    }

    @Override
    public Optional<TourPackageResponseDTO> getTourPackageById(Long id) {
        return tourPackageRepository.findById(id)
                .map(tourPackageMapper::toResponseDTO);
    }

    @Override
    public TourPackageResponseDTO updateTourPackage(Long id, TourPackageRequestDTO requestDTO) {
        return tourPackageRepository.findById(id)
                .map(tourPackage -> {
                    tourPackageMapper.updateEntity(tourPackage, requestDTO);
                    TourPackage updatedTourPackage = tourPackageRepository.save(tourPackage);
                    return tourPackageMapper.toResponseDTO(updatedTourPackage);
                })
                .orElse(null);
    }

    @Override
    public void deleteTourPackage(Long id) {
        tourPackageRepository.deleteById(id);
    }
}
