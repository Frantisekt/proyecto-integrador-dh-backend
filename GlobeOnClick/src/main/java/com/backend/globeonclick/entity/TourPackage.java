package com.backend.globeonclick.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private boolean state = false;

    @Builder.Default
    @ManyToMany(mappedBy = "tourPackages", fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    @ManyToMany(mappedBy = "tourPackages", fetch = FetchType.LAZY)
    private List<MediaPackage> mediaPackages = new ArrayList<>();

    @OneToMany(mappedBy = "tourPackage", fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "package_feature",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private List<Feature> features = new ArrayList<>();

    private LocalDate start_date;
    private LocalDate end_date;
    private Double price;

    @ManyToMany(mappedBy = "favoritePackages", fetch = FetchType.LAZY)
    private List<User> favoriteByUsers = new ArrayList<>();

    public void addMediaPackage(MediaPackage mediaPackage) {
        if (mediaPackages == null) {
            mediaPackages = new ArrayList<>();
        }
        if (!mediaPackages.contains(mediaPackage)) {
            mediaPackages.add(mediaPackage);
        }
    }

    public void removeMediaPackage(MediaPackage mediaPackage) {
        mediaPackages.remove(mediaPackage);
    }

    public void addFeature(Feature feature) {
        if (features == null) {
            features = new ArrayList<>();
        }
        if (!features.contains(feature)) {
            features.add(feature);
        }
    }

    public void removeFeature(Feature feature) {
        features.remove(feature);
    }

    // Equals and hashCode methods based on ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TourPackage that = (TourPackage) o;
        return packageId != null && packageId.equals(that.packageId);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // ToString method that avoids circular references
    @Override
    public String toString() {
        return "TourPackage{" +
                "packageId=" + packageId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                '}';
    }
}