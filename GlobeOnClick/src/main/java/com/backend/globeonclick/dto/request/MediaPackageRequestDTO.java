package com.backend.globeonclick.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaPackageRequestDTO {
    private MultipartFile file;
    private String mediaTitle;
    private String mediaDescription;
}