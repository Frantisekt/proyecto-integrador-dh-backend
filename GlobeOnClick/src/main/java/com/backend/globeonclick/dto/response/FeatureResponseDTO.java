package com.backend.globeonclick.dto.response;

import com.backend.globeonclick.entity.FeatureName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureResponseDTO {
    private Long featureId;
    private FeatureName name;
}
