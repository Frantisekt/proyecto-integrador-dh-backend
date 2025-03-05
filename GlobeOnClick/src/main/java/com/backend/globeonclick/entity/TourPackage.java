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
@Table(name = "tour_packages")
public class TourPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    private String title;
    private String description;
    private boolean state = false;

    @Builder.Default
    @ManyToMany(mappedBy = "tourPackages")
    private List<Category> categories = new ArrayList<>();

    @ManyToMany(mappedBy = "tourPackages")
    private List<MediaPackage> mediaPackages = new ArrayList<>();

    @OneToMany(mappedBy = "tourPackage")
    private List<Reservation> reservations;

    @ManyToMany
    @JoinTable(
            name = "package_feature",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private List<Feature> features = new ArrayList<>();

    public void addMediaPackage(MediaPackage mediaPackage) {
        if (mediaPackages == null) {
            mediaPackages = new ArrayList<>();
        }
        mediaPackages.add(mediaPackage);
        mediaPackage.addTourPackage(this);
    }

    public void removeMediaPackage(MediaPackage mediaPackage) {
        mediaPackages.remove(mediaPackage);
        mediaPackage.removeTourPackage(this);
    }

    public void addFeature(Feature feature) {
        if (features == null) {
            features = new ArrayList<>();
        }
        features.add(feature);
        if (!feature.getPackages().contains(this)) {
            feature.getPackages().add(this);
        }
    }

    public void removeFeature(Feature feature) {
        features.remove(feature);
        feature.getPackages().remove(this);
    }
}