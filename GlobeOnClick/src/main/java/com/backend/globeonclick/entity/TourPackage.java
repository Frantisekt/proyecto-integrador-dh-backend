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
}