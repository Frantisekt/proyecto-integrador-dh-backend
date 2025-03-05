package com.backend.globeonclick.utils.mappers;

import com.backend.globeonclick.dto.request.MediaPackageRequestDTO;
import com.backend.globeonclick.dto.response.MediaPackageResponseDTO;
import com.backend.globeonclick.entity.MediaPackage;
import com.backend.globeonclick.entity.Media;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MediaPackageMapper {

    public MediaPackageResponseDTO toResponseDTO(MediaPackage mediaPackage) {
        if (mediaPackage == null) return null;

        return MediaPackageResponseDTO.builder()
                .mediaPackageId(mediaPackage.getMediaPackageId())
                .packageId(mediaPackage.getTourPackages() != null && !mediaPackage.getTourPackages().isEmpty()
                        ? mediaPackage.getTourPackages().getFirst().getPackageId()
                        : null)
                .packageTitle(mediaPackage.getTourPackages() != null && !mediaPackage.getTourPackages().isEmpty()
                        ? mediaPackage.getTourPackages().getFirst().getTitle()
                        : null)
                .mediaId(Optional.ofNullable(mediaPackage.getMedia()).map(Media::getMediaId).orElse(null))
                .mediaUrl(Optional.ofNullable(mediaPackage.getMedia()).map(Media::getUrl).orElse(null))
                .mediaTitle(mediaPackage.getMediaTitle())
                .mediaDescription(mediaPackage.getMediaDescription())
                .build();
    }

    public MediaPackage toEntity(MediaPackageRequestDTO requestDTO, Media media) {
        return MediaPackage.builder()
                .media(media)
                .mediaTitle(requestDTO.getMediaTitle())
                .mediaDescription(requestDTO.getMediaDescription())
                .build();
    }

    public void updateEntity(MediaPackage mediaPackage, MediaPackageRequestDTO requestDTO) {
        mediaPackage.setMediaTitle(requestDTO.getMediaTitle());
        mediaPackage.setMediaDescription(requestDTO.getMediaDescription());
    }
}