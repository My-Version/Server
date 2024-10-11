package com.myversion.myversion.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.myversion.myversion.domain.Song;
import com.myversion.myversion.service.Service;
import com.myversion.myversion.repository.SpringDataJpaSongRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
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

    // @GetMapping("/compare")
    // public List<String> compareSong(@RequestParam String userDir, @RequestParam String coverDir) {
    //     return Service.CompareSong(userDir, coverDir);
    // }

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

    // @DeleteMapping
    // public ResponseEntity<Song> deleteSong(@RequestParam Long id) {
    //     if (songRepository.existsById(id)){
    //         songRepository.deleteById(id);
    //         return ResponseEntity.noContent().build();
    //     }else{
    //         return ResponseEntity.notFound().build();
    //     }
    // }
}