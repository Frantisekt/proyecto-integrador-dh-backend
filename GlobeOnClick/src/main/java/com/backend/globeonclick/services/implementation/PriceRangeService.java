package com.backend.globeonclick.services.implementation;

import com.backend.globeonclick.entity.PriceRange;
import com.backend.globeonclick.entity.TourPackage;
import com.backend.globeonclick.repository.IPriceRangeRepository;
import com.backend.globeonclick.repository.ITourPackageRepository;
import com.backend.globeonclick.services.interfaces.IPriceRangeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceRangeService implements IPriceRangeService {

    private final IPriceRangeRepository priceRangeRepository;
    private final ITourPackageRepository tourPackageRepository;

    @PostConstruct
    protected void init() {
        try {
            initializePriceRanges();
            initializeExistingPackages();
        } catch (Exception e) {
            // Log error pero permite que la aplicación continúe
            e.printStackTrace();
        }
    }

    @Transactional
    @CacheEvict(value = {"priceRanges", "priceRangeSearch"}, allEntries = true)
    protected void initializePriceRanges() {
        if (priceRangeRepository.count() == 0) {
            List<PriceRange> ranges = Arrays.asList(
                new PriceRange(0.0, 500.0, "0-500"),
                new PriceRange(500.0, 1000.0, "500-1000"),
                new PriceRange(1000.0, 1500.0, "1000-1500"),
                new PriceRange(1500.0, 2000.0, "1500-2000"),
                new PriceRange(2000.0, 3000.0, "2000-3000"),
                new PriceRange(3000.0, 5000.0, "3000-5000"),
                new PriceRange(5000.0, Double.MAX_VALUE, "5000-999999")
            );
            priceRangeRepository.saveAll(ranges);
        }
    }

    @Transactional
    @CacheEvict(value = {"priceRanges", "priceRangeSearch"}, allEntries = true)
    public void initializeExistingPackages() {
        List<TourPackage> allPackages = tourPackageRepository.findAll();
        List<PriceRange> allRanges = priceRangeRepository.findAll();
        
        // Limpiar las asignaciones existentes
        allRanges.forEach(range -> range.setPackageIds(new HashSet<>()));
        
        // Asignar paquetes a sus rangos correspondientes
        for (TourPackage pkg : allPackages) {
            if (pkg.getPrice() != null) {
                for (PriceRange range : allRanges) {
                    if (pkg.getPrice() >= range.getMinPrice() && 
                        pkg.getPrice() <= (range.getMaxPrice().equals(Double.MAX_VALUE) ? Double.MAX_VALUE : range.getMaxPrice())) {
                        range.addPackageId(pkg.getPackageId());
                        break;
                    }
                }
            }
        }
        
        priceRangeRepository.saveAll(allRanges);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"priceRanges", "priceRangeSearch"}, allEntries = true)
    public void assignPackageToPriceRange(TourPackage tourPackage) {
        if (tourPackage.getPrice() == null) return;

        List<PriceRange> allRanges = priceRangeRepository.findAll();
        
        // Primero, remover el paquete de cualquier rango existente
        for (PriceRange range : allRanges) {
            range.removePackageId(tourPackage.getPackageId());
        }

        // Luego, asignar al rango correcto
        for (PriceRange range : allRanges) {
            if (tourPackage.getPrice() >= range.getMinPrice() && 
                tourPackage.getPrice() <= (range.getMaxPrice().equals(Double.MAX_VALUE) ? Double.MAX_VALUE : range.getMaxPrice())) {
                range.addPackageId(tourPackage.getPackageId());
                break;
            }
        }

        priceRangeRepository.saveAll(allRanges);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "priceRangeSearch", key = "#minPrice + '-' + #maxPrice")
    public List<TourPackage> findPackagesInPriceRange(Double minPrice, Double maxPrice) {
        // Encontrar todos los rangos que se superponen con el rango solicitado
        List<PriceRange> relevantRanges = priceRangeRepository.findRangesInRange(minPrice, maxPrice);
        
        // Recolectar todos los IDs de paquetes de los rangos relevantes
        Set<Long> packageIds = relevantRanges.stream()
            .flatMap(range -> range.getPackageIds().stream())
            .collect(Collectors.toSet());

        // Buscar los paquetes por sus IDs
        if (packageIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        return tourPackageRepository.findAllById(packageIds);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "priceRanges")
    public List<PriceRange> getAllRanges() {
        return priceRangeRepository.findAllOrderByMinPrice();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"priceRanges", "priceRangeSearch"}, allEntries = true)
    public void reinitializeAllRanges() {
        try {
            // Primero eliminar todas las relaciones de la tabla price_range_packages
            priceRangeRepository.findAll().forEach(range -> {
                range.getPackageIds().clear();
                priceRangeRepository.save(range);
            });
            
            // Forzar el flush para asegurar que se ejecuten las eliminaciones
            priceRangeRepository.flush();
            
            // Ahora sí podemos eliminar los rangos
            priceRangeRepository.deleteAll();
            priceRangeRepository.flush();

            // Crear nuevos rangos
            initializePriceRanges();
            
            // Reasignar paquetes
            initializeExistingPackages();
        } catch (Exception e) {
            throw new RuntimeException("Error al reinicializar los rangos de precios: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "priceRanges")
    public List<TourPackage> getAllPackagesWithPrices() {
        return tourPackageRepository.findAll();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"priceRanges", "priceRangeSearch"}, allEntries = true)
    public void updatePackagePriceRange(TourPackage tourPackage) {
        assignPackageToPriceRange(tourPackage);
    }
} 