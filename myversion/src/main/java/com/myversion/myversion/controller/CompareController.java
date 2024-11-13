package com.myversion.myversion.controller;


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

import com.myversion.myversion.domain.Compare;
import com.myversion.myversion.service.CompareService;
import com.myversion.myversion.service.CoverSongService;
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
    public CompareController(S3Client s3Client, S3UploadService s3UploadService, CompareService compareService, CoverSongService coverSongService) {
        this.s3Client = s3Client;
        this.s3UploadService = s3UploadService;
        this.compareService = compareService;
        this.coverSongService = coverSongService;
    }

    // Long : compareID반환 -> 결과 탐색에 사용
    @PostMapping("/compareUpload")
    public ResponseEntity<?> compareUpload(@RequestParam("file") MultipartFile file, Long coverId) throws IOException {
        String recordUrl = null;
        String userId = coverSongService.getUserIdmById(coverId);
        String coverSongUrl = coverSongService.findS3FileLocationById(coverId).orElse("");

        Long compareId = null;
        try {
            recordUrl = recordUpload(file).getBody().toString();
            // 사용자 이름, 녹음파일 링크, 커버파일 링크를 DB에 저장
            compareId = compareService.addCompare(userId, recordUrl, coverSongUrl);
        } catch (IOException e) {
            // DB 등록 실패

            return null;
        }
        // 비동기 비교 작업 시작
        CompletableFuture<List<String>> futureResult = CompareService.compareSongAsync(recordUrl, coverSongUrl);
        // 작업 완료 후 실행할 동작
        Long finalCompareId = compareId;
        futureResult.thenAccept(result -> {
            String creatTime = extractCompareResult(result, "createTime").split(": ")[1];
            String similarity = extractCompareResult(result, "similarity").split(": ")[1];
            String worst_time = extractCompareResult(result, "worst_time").split(": ")[1];
            String best_time = extractCompareResult(result, "best_time").split(": ")[1];
            String time_length = extractCompareResult(result, "time_length").split(": ")[1];

            // https:로 시작하고 .png로 끝나는 문자열을 필터링하여 찾기
            String pngFileLocation = result.stream()
                    .filter(item -> item.startsWith("https:") && item.endsWith(".png"))
                    .findFirst()
                    .orElse(null);

            // DB 등록 -> 모든 json file 데이터를 DB에 속성 값으로 업데이트
            compareService.updateResult(finalCompareId, creatTime, similarity, worst_time, best_time, time_length, pngFileLocation);
        });
        return ResponseEntity.ok(compareId);
    }

    private String extractCompareResult(List<String>result, String factor) {
        return result.stream().filter(item ->item.startsWith(factor)).findFirst().orElse(null);
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
