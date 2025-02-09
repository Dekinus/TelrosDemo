package com.example.demo.model.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UserImageRequest(

        @NotNull(message = "Image must be not null")
        MultipartFile file
) {
}
