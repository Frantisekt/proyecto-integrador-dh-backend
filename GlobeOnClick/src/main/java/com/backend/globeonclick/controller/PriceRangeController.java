package com.backend.globeonclick.controller;

import com.backend.globeonclick.dto.request.PriceRangeSearchRequestDTO;
import com.backend.globeonclick.dto.response.PriceRangeResponseDTO;
import com.backend.globeonclick.dto.response.TourPackageResponseDTO;
import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.services.implementation.PriceRangeService;
import com.backend.globeonclick.services.implementation.TourPackageService;
import com.backend.globeonclick.utils.mappers.PriceRangeMapper;
import com.backend.globeonclick.utils.mappers.MediaPackageMapper;
import com.backend.globeonclick.utils.mappers.TourPackageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/api/price-ranges")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class PriceRangeController {

    private final PriceRangeService priceRangeService;
    private final TourPackageService tourPackageService;
    private final PriceRangeMapper priceRangeMapper;
    private final MediaPackageMapper mediaPackageMapper;
    private final TourPackageMapper tourPackageMapper;

    @GetMapping
    public ResponseEntity<List<PriceRangeResponseDTO>> getAllRanges() {
        return ResponseEntity.ok(
            priceRangeService.getAllRanges().stream()
                .map(priceRangeMapper::toDTO)
                .collect(Collectors.toList())
        );
    }

    @PostMapping("/search")
    public ResponseEntity<List<TourPackageResponseDTO>> searchByPriceRange(
            @RequestBody PriceRangeSearchRequestDTO searchRequest) {
        List<TourPackageResponseDTO> packages = priceRangeService.findPackagesInPriceRange(
                searchRequest.getMinPrice(),
                searchRequest.getMaxPrice()
            ).stream()
            .map(tourPackageMapper::toResponseDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(packages);
    }

    @PostMapping("/reinitialize")
    public ResponseEntity<Void> reinitializeRanges() {
        priceRangeService.reinitializeAllRanges();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/diagnostic")
    public ResponseEntity<Map<String, Object>> getDiagnosticInfo() {
        List<TourPackage> packages = priceRangeService.getAllPackagesWithPrices();
        Map<String, Object> diagnosticInfo = new HashMap<>();
        diagnosticInfo.put("totalPackages", packages.size());
        diagnosticInfo.put("packagesWithPrices", packages.stream().filter(p -> p.getPrice() != null).count());
        diagnosticInfo.put("prices", packages.stream()
            .map(TourPackage::getPrice)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
        return ResponseEntity.ok(diagnosticInfo);
    }
} 