package com.backend.globeonclick.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaCategoryResponseDTO {
    private Long mediaCategoryId;
    private Long categoryId;
    private String categoryTitle;
    private Long mediaId;
    private String mediaUrl;
    private String mediaTitle;
    private String mediaDescription;
}