package com.myversion.myversion.controller;


import com.myversion.myversion.domain.Compare;
import com.myversion.myversion.service.CoverSongService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myversion.myversion.service.CompareService;
import com.myversion.myversion.service.S3UploadService;

import software.amazon.awssdk.services.s3.S3Client;

@RestController
@RequestMapping
public class CompareController {

    private final S3Client s3Client;
    private final S3UploadService s3UploadService;
    private final CompareService compareService;
    private final CoverSongService coverSongService;

    @Autowired
    public CompareController(S3Client s3Client, S3UploadService s3UploadService, CompareService compareService,
                             CoverSongService coverSongService) {
        this.s3Client = s3Client;
        this.s3UploadService = s3UploadService;
        this.compareService = compareService;
        this.coverSongService = coverSongService;
    }

    // Long : compareID반환 -> 결과 탐색에 사용
    @PostMapping("/compareUpload")
    public ResponseEntity<?> compareUpload(@RequestParam("file") MultipartFile file, Long coverId) throws IOException {
        String recordUrl = recordUpload(file).getBody().toString();
        String coverSongUrl =coverSongService.findS3FileLocationById(coverId).orElse("");

        // 1. CompareDB에 빈 Compare 생성 -> id 반환하기
        Long compareId = compareService.addCompare();

        // 비동기 비교 작업 시작
        CompletableFuture<List<String>> futureResult = CompareService.compareSongAsync(recordUrl, coverSongUrl);
        // 작업 완료 후 실행할 동작
        futureResult.thenAccept(result -> {
            String jsonFileLocation = result.stream()
                    .filter(item -> item.startsWith("https:") && item.endsWith(".json"))
                    .findFirst()
                    .orElse(null);

            // https:로 시작하고 .png로 끝나는 문자열을 필터링하여 찾기
            String pngFileLocation = result.stream()
                    .filter(item -> item.startsWith("https:") && item.endsWith(".png"))
                    .findFirst()
                    .orElse(null);
            compareService.updateCompareFileLocations(compareId, jsonFileLocation, pngFileLocation);
        });
        return ResponseEntity.ok(compareId);
    }

    // compare DB에서 Json 파일의 S3 링크를 넘김
    @GetMapping("/compareResult")
    public String compareResult(@RequestParam Long compareId) throws IOException {
        return compareService.getCompareResultS3Path(compareId, true);
    }

    // compare DB에서 이미지 파일의 S3 링크를 넘김
    @GetMapping("/compareImage")
    public String compareImage(@RequestParam Long compareId) throws IOException {
        return compareService.getCompareResultS3Path(compareId, false);
    }

    // userId의 compareList
    @GetMapping("/compareList")
    public List<Compare> compareList(@RequestParam String userId) throws IOException {
        return compareService.getCompareListByUserId(userId);
    }

    private ResponseEntity<?> recordUpload(MultipartFile file) throws IOException {
        return ResponseEntity.ok(
            s3UploadService.uploadFile(file, "user-record", ("userRecords/" + file.getOriginalFilename())));
    }
}
