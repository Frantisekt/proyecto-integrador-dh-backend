package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.dto.response.MediaResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IMediaService {
    MediaResponseDTO uploadMedia(MultipartFile file);

    List<MediaResponseDTO> getAllMedia();

    Optional<MediaResponseDTO> getMediaById(Long id);

    void deleteMedia(Long id);
}