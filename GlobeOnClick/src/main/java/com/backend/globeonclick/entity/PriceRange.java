package com.backend.globeonclick.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "price_ranges")
public class PriceRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double minPrice;

    @Column(nullable = false)
    private Double maxPrice;

    @Column(length = 50)
    private String rangeLabel;

    @ElementCollection
    @CollectionTable(
        name = "price_range_packages",
        joinColumns = @JoinColumn(name = "price_range_id")
    )
    @Column(name = "package_id")
    @Builder.Default
    private Set<Long> packageIds = new HashSet<>();

    // Constructor útil para crear rangos
    public PriceRange(Double minPrice, Double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.rangeLabel = String.format("%.0f-%.0f", minPrice, maxPrice == Double.MAX_VALUE ? 999999 : maxPrice);
    }

    // Constructor con label
    public PriceRange(Double minPrice, Double maxPrice, String rangeLabel) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.rangeLabel = rangeLabel;
    }

    // Método para verificar si un precio está en este rango
    public boolean containsPrice(Double price) {
        return price >= minPrice && price < maxPrice;
    }

    // Métodos para manejar los IDs de paquetes
    public void addPackageId(Long packageId) {
        if (this.packageIds == null) {
            this.packageIds = new HashSet<>();
        }
        this.packageIds.add(packageId);
    }

    public void removePackageId(Long packageId) {
        if (this.packageIds != null) {
            this.packageIds.remove(packageId);
        }
    }

    public int getPackageCount() {
        return packageIds != null ? packageIds.size() : 0;
    }
} 