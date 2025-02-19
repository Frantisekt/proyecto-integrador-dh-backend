package com.backend.globeonclick.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaCategoryRequestDTO {
    private MultipartFile file;
    private String mediaTitle;
    private String mediaDescription;
}