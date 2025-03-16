package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.MediaCategoryRequestDTO;
import com.backend.globeonclick.dto.response.MediaCategoryResponseDTO;
import com.backend.globeonclick.entity.Media;
import com.backend.globeonclick.entity.MediaCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MediaCategoryMapper {

    public MediaCategoryResponseDTO toResponseDTO(MediaCategory mediaCategory) {
        if (mediaCategory == null) return null;

        return MediaCategoryResponseDTO.builder()
                .mediaCategoryId(mediaCategory.getMediaCategoryId())
                .categoryId(mediaCategory.getCategories() != null && !mediaCategory.getCategories().isEmpty()
                        ? mediaCategory.getCategories().getFirst().getCategoryId()
                        : null)
                .categoryTitle(mediaCategory.getCategories() != null && !mediaCategory.getCategories().isEmpty()
                        ? mediaCategory.getCategories().getFirst().getTitle()
                        : null)
                .mediaId(Optional.ofNullable(mediaCategory.getMedia()).map(Media::getMediaId).orElse(null))
                .mediaUrl(Optional.ofNullable(mediaCategory.getMedia()).map(Media::getUrl).orElse(null))
                .mediaTitle(mediaCategory.getMediaTitle())
                .mediaDescription(mediaCategory.getMediaDescription())
                .build();
    }

    public MediaCategory toEntity(MediaCategoryRequestDTO requestDTO, Media media) {
        return MediaCategory.builder()
                .media(media)
                .mediaTitle(requestDTO.getMediaTitle())
                .mediaDescription(requestDTO.getMediaDescription())
                .build();
    }

    public void updateEntity(MediaCategory mediaCategory, MediaCategoryRequestDTO requestDTO) {
        mediaCategory.setMediaTitle(requestDTO.getMediaTitle());
        mediaCategory.setMediaDescription(requestDTO.getMediaDescription());
    }
}
