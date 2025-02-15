package com.backend.globeonclick.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Package packageId;

    private String title;
    private String description;
    private String image;
    private Double price;
    private String currency;
    private String restrictions;
    private boolean state;
    private Double discount;

    @OneToMany(mappedBy = "category")
    private List<MediaCategory> mediaCategories;

    @OneToMany(mappedBy = "category")
    private List<Reservation> reservations;
}