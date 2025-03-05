package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.response.FeatureResponseDTO;
import com.backend.globeonclick.entity.FeatureName;
import com.backend.globeonclick.services.implementation.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
public class FeatureController {
    private final FeatureService featureService;

    @PostMapping("/create")
    public ResponseEntity<FeatureResponseDTO> createFeature(@RequestParam FeatureName featureName) {
        return new ResponseEntity<>(featureService.createFeature(featureName), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FeatureResponseDTO>> getAllFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    @PostMapping("/add-to-package")
    public ResponseEntity<Void> addFeatureToPackage(
            @RequestParam Long packageId,
            @RequestParam FeatureName featureName) {
        featureService.addFeatureToPackage(packageId, featureName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-from-package")
    public ResponseEntity<Void> removeFeatureFromPackage(
            @RequestParam Long packageId,
            @RequestParam FeatureName featureName) {
        featureService.removeFeatureFromPackage(packageId, featureName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<FeatureResponseDTO>> getFeaturesForPackage(@PathVariable Long packageId) {
        return ResponseEntity.ok(featureService.getFeaturesForPackage(packageId));
    }
}