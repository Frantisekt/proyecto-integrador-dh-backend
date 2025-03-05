package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.dto.response.MediaResponseDTO;
import com.backend.globeonclick.entity.Media;
import com.backend.globeonclick.repository.IMediaRepository;
import com.backend.globeonclick.services.interfaces.IMediaService;
import com.backend.globeonclick.utils.mappers.MediaMapper;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MediaService implements IMediaService {

    private final IMediaRepository mediaRepository;
    private final MediaMapper mediaMapper;
    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder.name}")
    private String cloudinaryFolder;

    @Override
    public MediaResponseDTO uploadMedia(MultipartFile file) {
        try {
            Map<String, String> uploadOptions = Map.of(
                    "folder", cloudinaryFolder,
                    "resource_type", "auto"
            );

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    uploadOptions
            );

            Media media = Media.builder()
                    .url((String) uploadResult.get("secure_url"))
                    .cloudinaryId((String) uploadResult.get("public_id"))
                    .build();

            media = mediaRepository.save(media);
            return mediaMapper.toResponseDTO(media);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading media to Cloudinary", e);
        }
    }

    @Override
    public List<MediaResponseDTO> getAllMedia() {
        return mediaRepository.findAll()
                .stream()
                .map(mediaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MediaResponseDTO> getMediaById(Long id) {
        return mediaRepository.findById(id)
                .map(mediaMapper::toResponseDTO);
    }

    @Override
    public MediaResponseDTO updateMedia(Long id, MultipartFile newFile) {
        deleteMedia(id);
        return uploadMedia(newFile);
    }

    @Override
    public void deleteMedia(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + id));

        try {
            cloudinary.uploader().destroy(media.getCloudinaryId(), Map.of());
            mediaRepository.delete(media);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting media from Cloudinary", e);
        }
    }
}