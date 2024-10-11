package com.myversion.myversion.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@RestController
public class MyversionController {

    private final S3Client s3Client;
    private final String bucket = "ku-myversion-bucket";

    public MyversionController(S3Client s3Client){
        this.s3Client = s3Client;
    }

    @PostMapping("/upload")
    public String callPythonApi(@RequestBody String text) {
        String apiUrl = "http://127.0.0.1:5000/upload";
        
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(apiUrl, "{\"text\": \"" + text + "\"}", String.class);

        // result는 파이썬 API의 응답을 나타냅니다.
        return result;
    }

    @GetMapping("/compare")
    public String showDesignForm(){
        return "design";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        ResponseInputStream<?> s3ObjectStream = s3Client.getObject(getObjectRequest);
        InputStreamResource resource = new InputStreamResource(s3ObjectStream);

        // 파일 확장자에 따른 MIME 타입 설정
        MediaType mediaType;
        if (fileName.endsWith(".mp3")) {
            mediaType = MediaType.parseMediaType("audio/mpeg");
        } else if (fileName.endsWith(".wav")) {
            mediaType = MediaType.parseMediaType("audio/wav");
        } else {
            mediaType = MediaType.APPLICATION_OCTET_STREAM; // 기본 MIME 타입
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}