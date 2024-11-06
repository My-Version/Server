package com.myversion.myversion.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import com.myversion.myversion.domain.CoverSong;
import com.myversion.myversion.service.CoverSongService;

@RestController
public class CoverController {
    
    private final S3Client s3Client;
    private final CoverSongService coverSongService;

    @Autowired
    public CoverController(S3Client s3Client, CoverSongService coverSongService){
        this.s3Client = s3Client;
        this.coverSongService = coverSongService;
    }


    @Autowired
    private ResourceLoader resourceLoader;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://192.168.123.101:5000/upload";

    @PostMapping("/upload")
    public String VoiceForCover(@RequestParam("file") MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        if (body.isEmpty()){
            return "failure";
        }else{
            return "true";
        }
        //HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        //ResponseEntity<String> response = restTemplate.exchange(flaskUrl,  HttpMethod.POST, request, String.class);
        
        //return response.getBody();
    }

    @GetMapping("/listDownload")
    public List<Map<String, String>> listDownload(@RequestParam String bucketName) {
        String bucket = "my-version-"+bucketName+"-list";
    
        ListObjectsV2Request listObjectsReqManual = ListObjectsV2Request.builder()
                .bucket(bucket)
                .build();

        ListObjectsV2Response listObjResponse = s3Client.listObjectsV2(listObjectsReqManual);

        return listObjResponse.contents().stream()
                .map(S3Object::key)
                .map(this::extractSongInfo)
                .collect(Collectors.toList());
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName, @RequestParam String bucketName) throws IOException {
        String bucket = "my-version-"+bucketName+"-list";
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
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