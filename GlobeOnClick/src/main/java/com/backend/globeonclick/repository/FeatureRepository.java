package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.Feature;
import com.backend.globeonclick.entity.FeatureName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    Optional<Feature> findByName(FeatureName name);
    boolean existsByName(FeatureName name);
}