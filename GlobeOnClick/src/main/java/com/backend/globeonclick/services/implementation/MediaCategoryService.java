package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.request.MediaCategoryRequestDTO;
import com.backend.globeonclick.dto.response.MediaCategoryResponseDTO;
import com.backend.globeonclick.dto.response.MediaResponseDTO;
import com.backend.globeonclick.entity.Category;
import com.backend.globeonclick.entity.Media;
import com.backend.globeonclick.entity.MediaCategory;
import com.backend.globeonclick.repository.ICategoryRepository;
import com.backend.globeonclick.repository.IMediaCategoryRepository;
import com.backend.globeonclick.repository.IMediaRepository;
import com.backend.globeonclick.services.interfaces.IMediaCategoryService;
import com.backend.globeonclick.services.interfaces.IMediaService;
import com.backend.globeonclick.utils.mappers.MediaCategoryMapper;
import com.backend.globeonclick.utils.mappers.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MediaCategoryService implements IMediaCategoryService {

    private final IMediaCategoryRepository mediaCategoryRepository;
    private final IMediaRepository mediaRepository;
    private final MediaCategoryMapper mediaCategoryMapper;
    private final IMediaService mediaService;
    private final MediaMapper mediaMapper;

    @Override
    public MediaCategoryResponseDTO createMediaCategory(MediaCategoryRequestDTO requestDTO) {
        MediaResponseDTO mediaResponse = mediaService.uploadMedia(requestDTO.getFile());
        Media media = mediaMapper.toEntity(mediaResponse);

        MediaCategory mediaCategory = mediaCategoryMapper.toEntity(requestDTO, media);
        MediaCategory savedMediaCategory = mediaCategoryRepository.save(mediaCategory);

        return mediaCategoryMapper.toResponseDTO(savedMediaCategory);
    }

    @Override
    public List<MediaCategoryResponseDTO> getAllMediaCategories() {
        List<MediaCategory> mediaCategories = mediaCategoryRepository.findAll();
        return mediaCategories.stream()
                .map(mediaCategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MediaCategoryResponseDTO> getMediaCategoryById(Long id) {
        return mediaCategoryRepository.findById(id)
                .map(mediaCategoryMapper::toResponseDTO);
    }

    @Override
    public MediaCategoryResponseDTO updateMediaCategory(Long id, MediaCategoryRequestDTO requestDTO) {
        MediaCategory mediaCategory = mediaCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MediaCategory not found with id: " + id));

        if (requestDTO.getFile() != null) {
            MediaResponseDTO mediaResponse = mediaService.uploadMedia(requestDTO.getFile());
            Media newMedia = mediaMapper.toEntity(mediaResponse);

            mediaCategory.setMedia(newMedia);
        }

        mediaCategory.setMediaTitle(requestDTO.getMediaTitle());
        mediaCategory.setMediaDescription(requestDTO.getMediaDescription());

        MediaCategory updatedMediaCategory = mediaCategoryRepository.save(mediaCategory);
        return mediaCategoryMapper.toResponseDTO(updatedMediaCategory);
    }

    @Override
    public void deleteMediaCategory(Long id) {
        if (!mediaCategoryRepository.existsById(id)) {
            throw new RuntimeException("MediaCategory not found with id: " + id);
        }

        /*Optional<MediaCategory> mediaCategory = mediaCategoryRepository.findById(id);

        Media media = mediaCategory.get().getMedia();

        Long mediaId = media.getMediaId();*/

        mediaCategoryRepository.deleteById(id);
        //mediaRepository.deleteById(mediaId);
    }
}