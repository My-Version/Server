package com.myversion.myversion.controller;


import com.myversion.myversion.service.S3UploadService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@RestController
public class CompareController {

    private final S3Client s3Client;
    private final S3UploadService s3UploadService;

    @Autowired
    public CompareController(S3Client s3Client, S3UploadService s3UploadService) {
        this.s3Client = s3Client;
        this.s3UploadService = s3UploadService;
    }

    // 접속 url 반환
     @PostMapping("/compareUpload")
     public ResponseEntity<?> compareUpload(@RequestParam("file") MultipartFile file, Long Id) throws IOException {
        String recordUrl = String.valueOf(recordUpload(file));

        return ResponseEntity.ok(recordUrl);
     }

    @GetMapping("/compareScore")
    public ResponseEntity<?> compareScore(@RequestParam String coverDir) throws IOException {
        String jsonData = "{\"userDir\":\"Hello, World!\"}";
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
            .headers(jsonHeaders)
            .body(jsonData);
    }

    @GetMapping("/compareImage")
    public ResponseEntity<?> compareImage(@RequestParam String imageFile) throws IOException {
        byte[] imageBytes = Files.readAllBytes(Paths.get("sim_wave_20241003_210352.png"));
        InputStreamResource imageResource = new InputStreamResource(new ByteArrayInputStream(imageBytes));

        HttpHeaders imageHeaders = new HttpHeaders();
        imageHeaders.setContentType(MediaType.IMAGE_PNG);

        return ResponseEntity.ok()
            .headers(imageHeaders)
            .body(imageResource);
    }

    private ResponseEntity<?> recordUpload(MultipartFile file) throws IOException {
        return ResponseEntity.ok(s3UploadService.uploadFile(file, "user-record",("userRecords/"+file.getOriginalFilename()) ));
    }
}
