package com.myversion.myversion.controller;


import com.myversion.myversion.service.CompareService;
import com.myversion.myversion.service.S3UploadService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@RestController
@RequestMapping("/api/compare")
public class CompareController {

    private final S3Client s3Client;
    private final S3UploadService s3UploadService;
    private final CompareService compareService;

    @Autowired
    public CompareController(S3Client s3Client, S3UploadService s3UploadService, CompareService compareService) {
        this.s3Client = s3Client;
        this.s3UploadService = s3UploadService;
        this.compareService = compareService;
    }

    // 접속 url 반환
    @PostMapping("/compareUpload")
    public ResponseEntity<?> compareUpload(@RequestParam("file") MultipartFile file, Long coverId) throws IOException {
        String recordUrl = String.valueOf(recordUpload(file));
        String coverSongUrl = String.valueOf(getS3LocationByIdUsingAPI(coverId));

        // 1. CompareDB에 빈 Compare 생성 -> id 반환하기
        Long compareId = compareService.addCompare();

        // 비동기 비교 작업 시작
        CompletableFuture<List<String>> futureResult = CompareService.compareSongAsync(recordUrl, coverSongUrl);
        // 작업 완료 후 실행할 동작 정의
        futureResult.thenAccept(result -> {
            // 2. id로 CompareDB 에 json이랑 img파일 링크 저장
            String jsonFileLocation = result.get(2);
            String pngFileLocation = result.get(3);
            compareService.updateLocations(compareId, jsonFileLocation, pngFileLocation);
        });

        return ResponseEntity.ok(compareId);
    }


    private Optional<String> getS3LocationByIdUsingAPI(Long Id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9091/cover/s3location/" + Id;
        return Optional.ofNullable(restTemplate.getForObject(url, String.class));
    }

    @GetMapping("/compareResult")
    public ResponseEntity<?> compareResult(@RequestParam String compareResultDir) throws IOException {
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
        return ResponseEntity.ok(
                s3UploadService.uploadFile(file, "user-record", ("userRecords/" + file.getOriginalFilename())));
    }
}
