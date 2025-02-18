package com.backend.globeonclick.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponseDTO {
    private Long mediaId;
    private String url;
    private String cloudinaryId;
}
