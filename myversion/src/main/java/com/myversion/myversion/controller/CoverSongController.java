package com.myversion.myversion.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import com.myversion.myversion.service.S3UploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myversion.myversion.domain.CoverSong;
import com.myversion.myversion.service.CoverSongService;

@RestController
@RequestMapping
public class CoverSongController {

    private final S3Client s3Client;
    private final CoverSongService coverSongService;
    private final S3UploadService s3UploadService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://192.168.123.101:5000/upload";


    @Autowired
    public CoverSongController(S3Client s3Client, S3UploadService s3UploadService, CoverSongService coverSongService) {
        this.s3Client = s3Client;
        this.coverSongService = coverSongService;
        this.s3UploadService = s3UploadService;
    }


    @PostMapping("/upload")
    public String VoiceForCover(@RequestParam("file") MultipartFile file, @RequestParam String userID,
                                @RequestParam String artist, @RequestParam String music) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String formattedDateTime = now.format(formatter);

        CoverSong coversong = new CoverSong(userID, artist, music, null, formattedDateTime);

        coverSongService.save(coversong);

        Map<String, String> musicInformation = new HashMap<String, String>();
        musicInformation = extractSongInfo(music);
        ObjectMapper objectMapper = new ObjectMapper();
        String musicInfo = objectMapper.writeValueAsString(musicInformation);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("file", file.getResource());
        body.add("music", musicInfo);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        
        ResponseEntity<byte[]> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, request, byte[].class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            String fileName = music + "-" + userID + "-" + formattedDateTime+".wav";
            File songFile = new File(fileName);
            try (FileOutputStream fos = new FileOutputStream(songFile)) {
                fos.write(response.getBody());
            }
            s3UploadService.uploadFile(songFile, "cover", (fileName));   
            coversong.setS3FileLocation("https://my-version-cover-list.s3.ap-northeast-2.amazonaws.com/" + fileName);
            coverSongService.updateCoverSong(coversong.getId(), coversong);
        } else {
            throw new IOException("Flask 서버에서 파일을 성공적으로 받지 못했습니다.");
        }
        return "success";

    }


    @GetMapping("/songList")
    public List<Map<String, String>> songList() {

        ListObjectsV2Request listObjectsReqManual = ListObjectsV2Request.builder()
                .bucket("my-version-song-list")
                .build();

        ListObjectsV2Response listObjResponse = s3Client.listObjectsV2(listObjectsReqManual);

        return listObjResponse.contents().stream()
                .map(S3Object::key)
                .map(this::extractSongInfo)
                .collect(Collectors.toList());
    }

    @GetMapping("/coverList")
    public List<CoverSong> coverList(@RequestParam String userId){
        return coverSongService.findAllByUserId(userId);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) throws IOException {
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("my-version-cover-list")
                .key(decodedFileName)
                .build();

        ResponseInputStream<?> s3ObjectStream = s3Client.getObject(getObjectRequest);
        InputStreamResource resource = new InputStreamResource(s3ObjectStream);

        MediaType mediaType;
        if (decodedFileName.endsWith(".mp3")) {
            mediaType = MediaType.parseMediaType("audio/mpeg");
        } else if (decodedFileName.endsWith(".wav")) {
            mediaType = MediaType.parseMediaType("audio/wav");
        } else {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + decodedFileName + "\"")
                .body(resource);
    }


    private Map<String, String> extractSongInfo(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        String nameAndArtist = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);

        String[] parts = nameAndArtist.split("-");
        if (parts.length == 2) {
            return Map.of("music", parts[0].trim(), "singer", parts[1].trim());
        } else {
            return Map.of("music", nameAndArtist.trim(), "singer", "Unknown");
        }
    }

}
