package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.request.MediaPackageRequestDTO;
import com.backend.globeonclick.dto.response.MediaPackageResponseDTO;
import com.backend.globeonclick.services.interfaces.IMediaPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media-packages")
@RequiredArgsConstructor
public class MediaPackageController {

    private final IMediaPackageService mediaPackageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaPackageResponseDTO> createMediaPackage(
            @ModelAttribute MediaPackageRequestDTO requestDTO) {
        return ResponseEntity.ok(mediaPackageService.createMediaPackage(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<MediaPackageResponseDTO>> getAllMediaPackages() {
        return ResponseEntity.ok(mediaPackageService.getAllMediaPackages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaPackageResponseDTO> getMediaPackageById(@PathVariable Long id) {
        return mediaPackageService.getMediaPackageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaPackageResponseDTO> updateMediaPackage(
            @PathVariable Long id,
            @ModelAttribute MediaPackageRequestDTO requestDTO) {
        return ResponseEntity.ok(mediaPackageService.updateMediaPackage(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaPackage(@PathVariable Long id) {
        mediaPackageService.deleteMediaPackage(id);
        return ResponseEntity.noContent().build();
    }
}
