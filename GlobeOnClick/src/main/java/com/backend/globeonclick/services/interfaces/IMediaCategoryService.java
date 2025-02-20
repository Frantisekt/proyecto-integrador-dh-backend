package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.request.MediaCategoryRequestDTO;
import com.backend.globeonclick.dto.response.MediaCategoryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IMediaCategoryService {
    MediaCategoryResponseDTO createMediaCategory(MediaCategoryRequestDTO requestDTO);

    List<MediaCategoryResponseDTO> getAllMediaCategories();

    Optional<MediaCategoryResponseDTO> getMediaCategoryById(Long id);

    MediaCategoryResponseDTO updateMediaCategory(Long id, MediaCategoryRequestDTO requestDTO);

    void deleteMediaCategory(Long id);
}