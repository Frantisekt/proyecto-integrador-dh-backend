package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.request.MediaPackageRequestDTO;
import com.backend.globeonclick.dto.response.MediaPackageResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IMediaPackageService {
    MediaPackageResponseDTO createMediaPackage(MediaPackageRequestDTO requestDTO);
    List<MediaPackageResponseDTO> getAllMediaPackages();
    Optional<MediaPackageResponseDTO> getMediaPackageById(Long id);
    MediaPackageResponseDTO updateMediaPackage(Long id, MediaPackageRequestDTO requestDTO);
    void deleteMediaPackage(Long id);
}