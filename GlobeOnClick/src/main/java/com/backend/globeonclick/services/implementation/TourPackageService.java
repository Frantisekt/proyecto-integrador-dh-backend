package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.request.TourPackageRequestDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;

import com.backend.globeonclick.entity.MediaPackage;
import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.exception.ResourceNotFoundException;
import com.backend.globeonclick.repository.IMediaPackageRepository;
import com.backend.globeonclick.repository.ITourPackageRepository;
import com.backend.globeonclick.services.interfaces.ITourPackageService;
import com.backend.globeonclick.utils.mappers.TourPackageMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    public Page<TourPackageResponseDTO> getAllTourPackagesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Long> idPage = tourPackageRepository.findAllIds(pageable);

        List<TourPackageResponseDTO> dtoList = new ArrayList<>(idPage.getContent().size());

        List<Long> idList = idPage.getContent();
        if (!idList.isEmpty()) {
            List<TourPackage> packages = entityManager.createQuery(
                            "SELECT DISTINCT tp FROM TourPackage tp " +
                                    "LEFT JOIN FETCH tp.categories c " +
                                    "WHERE tp.packageId IN :ids", TourPackage.class)
                    .setParameter("ids", idList)
                    .getResultList();

            Map<Long, TourPackage> packageMap = packages.stream()
                    .collect(Collectors.toMap(TourPackage::getPackageId, tp -> tp));

            if (!packages.isEmpty()) {
                entityManager.createQuery(
                                "SELECT DISTINCT tp FROM TourPackage tp " +
                                        "LEFT JOIN FETCH tp.features " +
                                        "WHERE tp.packageId IN :ids", TourPackage.class)
                        .setParameter("ids", idList)
                        .getResultList()
                        .forEach(tp -> {
                            TourPackage existingPackage = packageMap.get(tp.getPackageId());
                            if (existingPackage != null) {
                                existingPackage.setFeatures(tp.getFeatures());
                            }
                        });

                Map<Long, List<MediaPackage>> mediaPackagesMap = new HashMap<>();
                List<MediaPackage> mediaPackages = entityManager.createQuery(
                                "SELECT mp FROM MediaPackage mp " +
                                        "JOIN mp.tourPackages tp " +
                                        "JOIN FETCH mp.media " +
                                        "WHERE tp.packageId IN :ids " +
                                        "ORDER BY mp.mediaPackageId", MediaPackage.class)
                        .setParameter("ids", idList)
                        .setMaxResults(idList.size() * 20)
                        .getResultList();

                for (MediaPackage mp : mediaPackages) {
                    for (TourPackage tp : mp.getTourPackages()) {
                        if (idList.contains(tp.getPackageId())) {
                            mediaPackagesMap.computeIfAbsent(tp.getPackageId(), k -> new ArrayList<>())
                                    .add(mp);
                        }
                    }
                }

                mediaPackagesMap.forEach((packageId, mps) -> {
                    TourPackage tp = packageMap.get(packageId);
                    if (tp != null) {
                        tp.setMediaPackages(mps.size() <= 20 ? mps : mps.subList(0, 20));
                    }
                });
            }

            for (Long id : idList) {
                TourPackage tourPackage = packageMap.get(id);
                if (tourPackage != null) {
                    dtoList.add(tourPackageMapper.toResponseDTO(tourPackage));
                }
            }
        }

        return new PageImpl<>(dtoList, pageable, idPage.getTotalElements());
    }

    @Override
    public Optional<TourPackageResponseDTO> getTourPackageById(Long id) {
        TourPackage tourPackage = tourPackageRepository.findByIdWithCategories(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourPackage not found with id: " + id));

        entityManager.createQuery(
                        "SELECT tp FROM TourPackage tp LEFT JOIN FETCH tp.features WHERE tp.packageId = :id",
                        TourPackage.class)
                .setParameter("id", id)
                .getSingleResult();

        List<MediaPackage> mediaPackages = entityManager.createQuery(
                        "SELECT mp FROM MediaPackage mp JOIN FETCH mp.media WHERE mp.id IN " +
                                "(SELECT mp2.id FROM MediaPackage mp2 JOIN mp2.tourPackages tp WHERE tp.packageId = :id) " +
                                "ORDER BY mp.mediaPackageId",
                        MediaPackage.class)
                .setParameter("id", id)
                .setMaxResults(20)
                .getResultList();

        tourPackage.setMediaPackages(mediaPackages);

        return Optional.ofNullable(tourPackageMapper.toResponseDTO(tourPackage));
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
            TourPackage tourPackage = tourPackageRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("TourPackage not found with id: " + id));

            Query mediaQuery = entityManager.createNativeQuery(
                    "DELETE FROM package_media_package WHERE package_id = ?");
            mediaQuery.setParameter(1, id);
            mediaQuery.executeUpdate();

            Query featureQuery = entityManager.createNativeQuery(
                    "DELETE FROM package_feature WHERE package_id = ?");
            featureQuery.setParameter(1, id);
            featureQuery.executeUpdate();

            Query categoryQuery = entityManager.createNativeQuery(
                    "DELETE FROM package_category WHERE package_id = ?");
            categoryQuery.setParameter(1, id);
            categoryQuery.executeUpdate();

            if (tourPackage.getReservations() != null && !tourPackage.getReservations().isEmpty()) {
                throw new RuntimeException("Cannot delete tour package with associated reservations");
            }

            tourPackageRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting tour package: " + e.getMessage());
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