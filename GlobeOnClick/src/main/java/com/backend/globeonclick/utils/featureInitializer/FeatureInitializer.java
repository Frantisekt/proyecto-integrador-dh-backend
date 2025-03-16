/*
package com.backend.globeonclick.utils.featureInitializer;

import com.backend.globeonclick.entity.Feature;
import com.backend.globeonclick.entity.FeatureName;
import com.backend.globeonclick.repository.FeatureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FeatureInitializer implements CommandLineRunner {

    private final FeatureRepository featureRepository;

    public FeatureInitializer(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    public void run(String... args) {
        Arrays.stream(FeatureName.values()).forEach(featureName -> {
            if (!featureRepository.existsByName(featureName.name())) {
                Feature feature = Feature.builder()
                        .name(featureName.name())
                        .displayName(featureName.getDisplayName())
                        .build();
                featureRepository.save(feature);
            }
        });
    }
}*/
