package com.backend.globeonclick.services.interfaces;

import com.backend.globeonclick.entity.PriceRange;
import com.backend.globeonclick.entity.TourPackage;

import java.util.List;

public interface IPriceRangeService {
    void assignPackageToPriceRange(TourPackage tourPackage);
    List<TourPackage> findPackagesInPriceRange(Double minPrice, Double maxPrice);
    List<PriceRange> getAllRanges();
    void updatePackagePriceRange(TourPackage tourPackage);
    void reinitializeAllRanges();
    void initializeExistingPackages();
} 