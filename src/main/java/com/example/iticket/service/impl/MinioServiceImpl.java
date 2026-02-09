package com.example.iticket.service.impl;

import com.example.iticket.service.concret.MinioService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    // Upload metodu
    public String upload(MultipartFile file){
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
            );
        } catch (ErrorResponseException | XmlParserException | IOException | InsufficientDataException |
                 InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException |
                 ServerException e) {
            throw new RuntimeException(e);
        }

        return fileName;
    }

    // Presigned URL yaratmaq
    public String getPresignedUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(fileName)
                    .expiry(60 * 60) // URL 1 saat aktiv olur
                    .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
                 ServerException e) {
            throw new RuntimeException(e);
        }
    }
}
