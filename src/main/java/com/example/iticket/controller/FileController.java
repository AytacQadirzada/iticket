package com.example.iticket.controller;

import com.example.iticket.service.concret.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final MinioService minioService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file){
        return minioService.upload(file);
    }

    @GetMapping("/{fileName}")
    public String getLink(@PathVariable String fileName){
        return minioService.getPresignedUrl(fileName);
    }
}
