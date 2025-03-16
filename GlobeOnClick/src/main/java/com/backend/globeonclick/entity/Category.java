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
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private TourPackage tourPackage;

    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private Double price;
    private String currency;
    private String restrictions;
    private boolean state;
    private Double discount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "package_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id")
    )
    private List<TourPackage> tourPackages = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    private List<MediaCategory> mediaCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<Reservation> reservations;

    public void addMediaCategory(MediaCategory mediaCategory) {
        if (mediaCategories == null) {
            mediaCategories = new ArrayList<>();
        }
        mediaCategories.add(mediaCategory);
        mediaCategory.addCategory(this);
    }

    public void removeMediaCategory(MediaCategory mediaCategory) {
        mediaCategories.remove(mediaCategory);
        mediaCategory.removeCategory(this);
    }
}