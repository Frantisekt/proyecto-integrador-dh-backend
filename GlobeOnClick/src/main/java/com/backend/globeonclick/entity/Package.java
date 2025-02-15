package com.backend.globeonclick.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "packages")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    private String title;
    private String description;
    private boolean state;

    @OneToMany(mappedBy = "packageId")
    private List<Category> categories;
}
