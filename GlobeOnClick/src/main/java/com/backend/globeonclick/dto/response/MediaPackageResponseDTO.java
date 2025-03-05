package com.backend.globeonclick.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaPackageResponseDTO {
    private Long mediaPackageId;
    private Long packageId;
    private String packageTitle;
    private Long mediaId;
    private String mediaUrl;
    private String mediaTitle;
    private String mediaDescription;
}