package com.example.demo.service;

import com.example.demo.config.properties.MinioProperties;
import com.example.demo.exception.ImageUploadException;
import com.example.demo.model.entity.UserImage;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    String uploadImage(UserImage image) {
        try {
            createBucket();
        } catch (Exception ex) {
            throw new ImageUploadException("Image upload failed " + ex.getMessage());
        }
        MultipartFile file = image.getFile();
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Image must not be empty and must have a name");
        }
        String fileName = generateFileName(file);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception ex) {
            throw new ImageUploadException("Image upload failed " + ex.getMessage());
        }
        saveImage(inputStream, fileName);
        return fileName;
    }

    String updateImage(UserImage image, String oldImageName) {
        deleteImage(oldImageName);
        return uploadImage(image);
    }

    @SneakyThrows
    private void createBucket() {

        System.out.println(minioProperties.getUrl());
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFileName(MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(MultipartFile file) {
        return file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }

    @SneakyThrows
    public void deleteImage(String fileName) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}
