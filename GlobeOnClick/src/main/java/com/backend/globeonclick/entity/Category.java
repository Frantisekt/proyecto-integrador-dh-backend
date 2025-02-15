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
    private String price;
    private String currency;
    private String restrictions;
    private boolean state;

    @OneToMany(mappedBy = "category")
    private List<MediaCategory> mediaCategories;
}