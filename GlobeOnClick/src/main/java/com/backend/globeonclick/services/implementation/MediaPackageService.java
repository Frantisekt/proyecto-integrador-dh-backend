package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.request.MediaPackageRequestDTO;
import com.backend.globeonclick.dto.response.MediaPackageResponseDTO;
import com.backend.globeonclick.dto.response.MediaResponseDTO;
import com.backend.globeonclick.entity.Media;
import com.backend.globeonclick.entity.MediaCategory;
import com.backend.globeonclick.entity.MediaPackage;
import com.backend.globeonclick.repository.IMediaPackageRepository;
import com.backend.globeonclick.repository.ITourPackageRepository;
import com.backend.globeonclick.services.interfaces.IMediaPackageService;
import com.backend.globeonclick.services.interfaces.IMediaService;
import com.backend.globeonclick.utils.mappers.MediaMapper;
import com.backend.globeonclick.utils.mappers.MediaPackageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MediaPackageService implements IMediaPackageService {

    private final IMediaPackageRepository mediaPackageRepository;
    private final MediaPackageMapper mediaPackageMapper;
    private final IMediaService mediaService;
    private final MediaMapper mediaMapper;


    @Override
    public MediaPackageResponseDTO createMediaPackage(MediaPackageRequestDTO requestDTO) {
        MediaResponseDTO mediaResponse = mediaService.uploadMedia(requestDTO.getFile());
        Media media = mediaMapper.toEntity(mediaResponse);

        MediaPackage mediaPackage = mediaPackageMapper.toEntity(requestDTO, media);
        MediaPackage savedMediaPackage = mediaPackageRepository.save(mediaPackage);

        return mediaPackageMapper.toResponseDTO(savedMediaPackage);
    }

    @Override
    public List<MediaPackageResponseDTO> getAllMediaPackages() {
        List<MediaPackage> mediaPackages = mediaPackageRepository.findAll();
        return mediaPackages.stream()
                .map(mediaPackageMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MediaPackageResponseDTO> getMediaPackageById(Long id) {
        return mediaPackageRepository.findById(id)
                .map(mediaPackageMapper::toResponseDTO);
    }

    @Override
    public MediaPackageResponseDTO updateMediaPackage(Long id, MediaPackageRequestDTO requestDTO) {
        MediaPackage mediaPackage = mediaPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MediaPackage not found with id: " + id));

        if (requestDTO.getFile() != null) {
            //Aqui debo quitar el update
            MediaResponseDTO mediaResponse = mediaService.updateMedia(mediaPackage.getMedia().getMediaId(), requestDTO.getFile());
            Media newMedia = mediaMapper.toEntity(mediaResponse);

            mediaPackage.setMedia(newMedia);
        }

        mediaPackage.setMediaTitle(requestDTO.getMediaTitle());
        mediaPackage.setMediaDescription(requestDTO.getMediaDescription());

        MediaPackage updatedMediaPackage = mediaPackageRepository.save(mediaPackage);
        return mediaPackageMapper.toResponseDTO(updatedMediaPackage);
    }

    @Override
    public void deleteMediaPackage(Long id) {
        if (!mediaPackageRepository.existsById(id)) {
            throw new RuntimeException("MediaPackage not found with id: " + id);
        }
        mediaPackageRepository.deleteById(id);
    }
}