package com.example.iticket.controller;

import com.example.iticket.service.concret.MinioService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final MinioService minioService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file){
        return minioService.upload(file);
    }


    @GetMapping("/{fileName}")
    public void getFile(
            @PathVariable String fileName,
            HttpServletResponse response
    ) throws IOException {

        String url = minioService.getPresignedUrl(fileName);
        response.sendRedirect(url);
    }
}
