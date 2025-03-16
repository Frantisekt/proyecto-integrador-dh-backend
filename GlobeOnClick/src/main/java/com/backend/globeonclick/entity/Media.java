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
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    private String url;
    private String cloudinaryId;

    @OneToMany(mappedBy = "media")
    private List<MediaCategory> mediaCategories;

    @OneToMany(mappedBy = "media")
    private List<MediaPackage> mediaPackages;
}