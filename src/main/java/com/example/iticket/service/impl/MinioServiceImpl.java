package com.example.iticket.service.impl;

import com.example.iticket.service.concret.MinioService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    public String upload(MultipartFile file){
        log.info("ActionLog.upload.start fileName: {} ", file.getOriginalFilename());
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

        log.info("ActionLog.upload.end fileName: {} ", fileName);
        return fileName;
    }

    public String getPresignedUrl(String fileName) {
        log.info("ActionLog.getPresignedUrl.start fileName: {} ", fileName);
        try {
        log.info("ActionLog.getPresignedUrl.end fileName: {} ", fileName);
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(fileName)
                    .expiry(60 * 60)
                    .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException |
                 ServerException e) {
            throw new RuntimeException(e);
        }
    }
}
