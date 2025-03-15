package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    boolean existsByName(String name);
    Optional<Feature> findByName(String name);
}