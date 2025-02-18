package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.request.MediaCategoryRequestDTO;
import com.backend.globeonclick.dto.response.MediaCategoryResponseDTO;
import com.backend.globeonclick.services.interfaces.IMediaCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media-categories")
public class MediaCategoryController {

    private final IMediaCategoryService mediaCategoryService;

    @Autowired
    public MediaCategoryController(IMediaCategoryService mediaCategoryService) {
        this.mediaCategoryService = mediaCategoryService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaCategoryResponseDTO> createMediaCategory(
            @ModelAttribute MediaCategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(mediaCategoryService.createMediaCategory(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<MediaCategoryResponseDTO>> getAllMediaCategories() {
        return ResponseEntity.ok(mediaCategoryService.getAllMediaCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaCategoryResponseDTO> getMediaCategoryById(@PathVariable Long id) {
        return mediaCategoryService.getMediaCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaCategoryResponseDTO> updateMediaCategory(
            @PathVariable Long id,
            @ModelAttribute MediaCategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(mediaCategoryService.updateMediaCategory(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaCategory(@PathVariable Long id) {
        mediaCategoryService.deleteMediaCategory(id);
        return ResponseEntity.noContent().build();
    }
}
