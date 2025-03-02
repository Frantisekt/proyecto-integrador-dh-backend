package com.backend.globeonclick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_package")
public class MediaPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaPackageId;

    @ManyToMany
    @JoinTable(
            name = "package_media_package",
            joinColumns = @JoinColumn(name = "media_package_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id")
    )
    private List<TourPackage> tourPackages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private Media media;

    private String mediaTitle;
    private String mediaDescription;

    public void addTourPackage(TourPackage tourPackage) {
        if (tourPackages == null) {
            tourPackages = new ArrayList<>();
        }
        tourPackages.add(tourPackage);
    }

    public void removeTourPackage(TourPackage tourPackage) {
        if (tourPackages != null) {
            tourPackages.remove(tourPackage);
        }
    }
}