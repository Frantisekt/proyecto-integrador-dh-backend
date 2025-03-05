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
@Table(name = "media_category")
public class MediaCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaCategoryId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "category_media_category",
            joinColumns = @JoinColumn(name = "media_category_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private Media media;

    private String mediaTitle;
    private String mediaDescription;

    public void addCategory(Category category) {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        categories.add(category);
    }

    public void removeCategory(Category category) {
        if (categories != null) {
            categories.remove(category);
        }
    }
}
