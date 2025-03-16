package com.backend.globeonclick.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "features")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureId;

    @Column(unique = true, length = 255)
    private String name;

    @Column(length = 255)
    private String displayName;

    @ManyToMany(mappedBy = "features")
    private List<TourPackage> packages = new ArrayList<>();

    public void addPackage(TourPackage tourPackage) {
        if (packages == null) {
            packages = new ArrayList<>();
        }
        packages.add(tourPackage);
        if (!tourPackage.getFeatures().contains(this)) {
            tourPackage.getFeatures().add(this);
        }
    }

    public void removePackage(TourPackage tourPackage) {
        packages.remove(tourPackage);
        tourPackage.getFeatures().remove(this);
    }
}