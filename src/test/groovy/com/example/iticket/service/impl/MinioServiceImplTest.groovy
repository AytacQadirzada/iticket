package com.example.iticket.service.impl

import com.example.iticket.service.concret.MinioService
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.http.Method
import io.minio.errors.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.web.multipart.MultipartFile

import java.security.InvalidKeyException

import static org.mockito.Mockito.*
import static org.junit.jupiter.api.Assertions.*

class MinioServiceImplTest {

    @Mock
    MinioClient minioClient

    @Mock
    MultipartFile file

    @InjectMocks
    MinioServiceImpl fileService

    String bucketName = "test-bucket"

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this)
        fileService.bucketName = bucketName
    }

    @Test
    void "upload should return fileName successfully"() {
        when(file.getOriginalFilename()).thenReturn("test.txt")
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".bytes))
        when(file.getSize()).thenReturn(4L)
        when(file.getContentType()).thenReturn("text/plain")

        def fileName = fileService.upload(file)

        assertTrue(fileName.contains("test.txt"))

        // file.getOriginalFilename() log üçün də çağırıldığından ən az 1 dəfə yoxlayırıq
        verify(file, atLeastOnce()).getOriginalFilename()
        verify(file).getInputStream()
        verify(file).getSize()
        verify(file).getContentType()
        verify(minioClient).putObject(any(PutObjectArgs.class))
    }

    @Test
    void "upload should throw RuntimeException when MinioClient fails"() {
        when(file.getOriginalFilename()).thenReturn("fail.txt")
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".bytes))
        when(file.getSize()).thenReturn(4L)
        when(file.getContentType()).thenReturn("text/plain")

        doThrow(new IOException("Minio error")).when(minioClient).putObject(any(PutObjectArgs.class))

        RuntimeException exception = assertThrows(RuntimeException) {
            fileService.upload(file)
        }

        assertEquals("java.io.IOException: Minio error", exception.message)
    }

    @Test
    void "getPresignedUrl should return url successfully"() {
        String fileName = "file.txt"
        String presignedUrl = "http://localhost/file.txt"

        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenReturn(presignedUrl)

        def url = fileService.getPresignedUrl(fileName)

        assertEquals(presignedUrl, url)
        verify(minioClient).getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class))
    }

    @Test
    void "getPresignedUrl should throw RuntimeException when MinioClient fails"() {
        String fileName = "file.txt"
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenThrow(new InvalidKeyException("Invalid key"))

        RuntimeException exception = assertThrows(RuntimeException) {
            fileService.getPresignedUrl(fileName)
        }

        assertTrue(exception.cause instanceof InvalidKeyException)
        assertEquals("Invalid key", exception.cause.message)
    }
}