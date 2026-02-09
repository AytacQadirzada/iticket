package com.example.iticket.service.concret;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    String upload(MultipartFile file);
    String getPresignedUrl(String fileName);
}
