package com.example.demo.model.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserImage {

    private MultipartFile file;

}
