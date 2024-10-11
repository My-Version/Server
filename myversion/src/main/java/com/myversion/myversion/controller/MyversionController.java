package com.myversion.myversion.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import com.myversion.myversion.domain.Song;
import com.myversion.myversion.repository.JpaSongRepository;
import com.myversion.myversion.service.Service;
import com.myversion.myversion.repository.SpringDataJpaSongRepository;
import com.myversion.myversion.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.util.List;

@RestController
public class MyversionController {

    private final S3Client s3Client;
    private final String bucket = "ku-myversion-bucket";

    public MyversionController(S3Client s3Client){
        this.s3Client = s3Client;
    }

    @Autowired
    private SpringDataJpaSongRepository songRepository;

    @Autowired
    private Service service;



    @PostMapping("/upload")
    public String VoiceForCover(@RequestBody String text) {
        String apiUrl = "http://127.0.0.1:5000/upload";
        
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(apiUrl, "{\"text\": \"" + text + "\"}", String.class);

        // result는 파이썬 API의 응답을 나타냅니다.
        return result;
    }

    @GetMapping("/compare")
    public List<String> compareSong(@RequestParam String userDir, @RequestParam String coverDir) {
        return songService.CompareSong(userDir, coverDir);

    @PostMapping("/register")
    public boolean Register(String id, String pw){
        if(true){
            return true;
        }else{
            return false;
        }
    }

    @PostMapping("/login")
    public boolean Login(String id, String pw){
        if(true){
            return true;
        }else{
            return false;
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        ResponseInputStream<?> s3ObjectStream = s3Client.getObject(getObjectRequest);
        InputStreamResource resource = new InputStreamResource(s3ObjectStream);

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

    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        Song savedSong = songRepository.save(song);
        return ResponseEntity.ok(savedSong);
    }

    @DeleteMapping
    public ResponseEntity<Song> deleteSong(@RequestParam Long id) {
        if (songRepository.existsById(id)){
            songRepository.deleteById(id);
            return ResponseEntity.noContent().build();// 204 No Content 응답
        }else{
            return ResponseEntity.notFound().build();   // 404 Not Found 응답
        }
    }


}
