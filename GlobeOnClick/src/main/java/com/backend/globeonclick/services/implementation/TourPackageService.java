package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;

import com.backend.globeonclick.entity.MediaPackage;
import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.repository.IMediaPackageRepository;
import com.backend.globeonclick.repository.ITourPackageRepository;
import com.backend.globeonclick.services.interfaces.ITourPackageService;
import com.backend.globeonclick.utils.mappers.TourPackageMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
    private final IMediaPackageRepository mediaPackageRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public TourPackageResponseDTO createTourPackage(TourPackageRequestDTO requestDTO) {
        TourPackage tourPackage = tourPackageMapper.toEntity(requestDTO);

        if (requestDTO.getMediaPackageIds() != null && !requestDTO.getMediaPackageIds().isEmpty()) {
            List<MediaPackage> mediaPackages = mediaPackageRepository.findAllById(requestDTO.getMediaPackageIds());
            for (MediaPackage mediaPackage : mediaPackages) {
                tourPackage.addMediaPackage(mediaPackage);
                mediaPackage.addTourPackage(tourPackage);
                mediaPackageRepository.save(mediaPackage);
            }
        }

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

                    tourPackage.getMediaPackages().clear();
                    if (requestDTO.getMediaPackageIds() != null && !requestDTO.getMediaPackageIds().isEmpty()) {
                        List<MediaPackage> mediaPackages = mediaPackageRepository.findAllById(requestDTO.getMediaPackageIds());
                        mediaPackages.forEach(tourPackage::addMediaPackage);
                    }

                    TourPackage updatedTourPackage = tourPackageRepository.save(tourPackage);
                    return tourPackageMapper.toResponseDTO(updatedTourPackage);
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteTourPackage(Long id) {
        try {
            // Primero, eliminar las referencias en package_category
            Query query = entityManager.createNativeQuery(
                    "DELETE FROM package_category WHERE package_id = ?");
            query.setParameter(1, id);
            query.executeUpdate();

            // Luego eliminar el paquete
            tourPackageRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el paquete turístico: " + e.getMessage());
        }
    }

    @Override
    public TourPackageResponseDTO addMediaToTourPackage(Long packageId, Long mediaPackageId) {
        TourPackage tourPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + packageId));

        MediaPackage mediaPackage = mediaPackageRepository.findById(mediaPackageId)
                .orElseThrow(() -> new RuntimeException("MediaPackage not found with id: " + mediaPackageId));

        tourPackage.addMediaPackage(mediaPackage);
        mediaPackage.addTourPackage(tourPackage);

        mediaPackageRepository.save(mediaPackage);
        return tourPackageMapper.toResponseDTO(tourPackageRepository.save(tourPackage));
    }

    @Override
    public void removeMediaFromTourPackage(Long packageId, Long mediaPackageId) {
        TourPackage tourPackage = tourPackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + packageId));

        MediaPackage mediaPackage = mediaPackageRepository.findById(mediaPackageId)
                .orElseThrow(() -> new RuntimeException("MediaPackage not found with id: " + mediaPackageId));

        tourPackage.removeMediaPackage(mediaPackage);
        mediaPackage.removeTourPackage(tourPackage);

        mediaPackageRepository.save(mediaPackage);
        tourPackageRepository.save(tourPackage);
    }
}

