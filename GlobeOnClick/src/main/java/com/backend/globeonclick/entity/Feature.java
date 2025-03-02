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

    private String name;
    private String description;
    private String iconUrl;
    private String iconCloudinaryId;
    private boolean state = true;

    @ManyToMany(mappedBy = "features")
    private List<Category> categories = new ArrayList<>();
}