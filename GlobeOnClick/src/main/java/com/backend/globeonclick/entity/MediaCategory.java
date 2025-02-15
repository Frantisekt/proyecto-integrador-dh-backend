package com.backend.globeonclick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_category")
public class MediaCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaCategoryId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private Media media;

    private String mediaTitle;
    private String mediaDescription;
}
