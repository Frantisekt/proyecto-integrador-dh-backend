package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.response.MediaResponseDTO;
import com.backend.globeonclick.entity.Media;
import org.springframework.stereotype.Component;

@Component
public class MediaMapper {
    public MediaResponseDTO toResponseDTO(Media media) {
        if (media == null) return null;

        return MediaResponseDTO.builder()
                .mediaId(media.getMediaId())
                .url(media.getUrl())
                .cloudinaryId(media.getCloudinaryId())
                .build();
    }

    public Media toEntity(MediaResponseDTO responseDTO) {
        if (responseDTO == null) return null;

        return Media.builder()
                .mediaId(responseDTO.getMediaId())
                .url(responseDTO.getUrl())
                .cloudinaryId(responseDTO.getCloudinaryId())
                .build();
    }
}