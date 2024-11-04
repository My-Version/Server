package com.myversion.myversion.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
// import com.myversion.myversion.repository.SpringDataJpaRepository;
//import com.myversion.myversion.service.Service;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
public class MyversionController {

//    private final S3Client s3Client;
    private final String bucket = "ku-myversion-bucket";
    
//    @Autowired
//    public MyversionController(S3Client s3Client){
//        this.s3Client = s3Client;
//    }


    @Autowired
    private ResourceLoader resourceLoader;

    //@Autowired
    //private SpringDataJpaRepository Repository;

    // @Autowired
    // private Service service;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://192.168.123.101:5000/upload";

    @PostMapping("/upload")
    public String VoiceForCover(@RequestParam("file") MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(flaskUrl,  HttpMethod.POST, request, String.class);
        
        return response.getBody();
    }

    @PostMapping("/compareScore")
    public ResponseEntity<?> compareScore(@RequestParam("file") MultipartFile file, @RequestBody String coverDir) throws IOException {
        // String json_location = "classpath:sim_result_20241003_210352.json";
        // Resource resource = resourceLoader.getResource(json_location);

        // String jsonData = new String(Files.readAllBytes(resource.getFile().toPath()));
        String jsonData = "{\"userDir\": \"Hello, World!\"}";

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

    @PostMapping("/register")   
    public ResponseEntity<Boolean> Register(@RequestBody Map<String, String> requestData){
        String id = requestData.get("id");
        String pw = requestData.get("pw");
        
        if (id != null && !id.isEmpty() && pw != null && !pw.isEmpty()) {
            return ResponseEntity.ok(true); 
        } else {
            return ResponseEntity.ok(false); 
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> Login(@RequestBody Map<String, String> requestData){
        String id = requestData.get("id");
        String pw = requestData.get("pw");
        
        if (id != null && !id.isEmpty() && pw != null && !pw.isEmpty()) {
            return ResponseEntity.ok(true); 
        } else {
            return ResponseEntity.ok(false);
        }
    }

//    @GetMapping("/download/{fileName}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucket)
//                .key(fileName)
//                .build();
//
//        ResponseInputStream<?> s3ObjectStream = s3Client.getObject(getObjectRequest);
//        InputStreamResource resource = new InputStreamResource(s3ObjectStream);
//
//        MediaType mediaType;
//        if (fileName.endsWith(".mp3")) {
//            mediaType = MediaType.parseMediaType("audio/mpeg");
//        } else if (fileName.endsWith(".wav")) {
//            mediaType = MediaType.parseMediaType("audio/wav");
//        } else {
//            mediaType = MediaType.APPLICATION_OCTET_STREAM; // 기본 MIME 타입
//        }
//
//        return ResponseEntity.ok()
//                .contentType(mediaType)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                .body(resource);
//    }

    //@PostMapping
    //public ResponseEntity<Song> createSong(@RequestBody Song song) {
        //Song savedSong = Repository.save(song);
        //return ResponseEntity.ok(savedSong);
    //}

    // @DeleteMapping
    // public ResponseEntity<Song> deleteSong(@RequestParam Long id) {
    //     if (Repository.existsById(id)){
    //         Repository.deleteById(id);
    //         return ResponseEntity.noContent().build();
    //     }else{
    //         return ResponseEntity.notFound().build();
    //     }
    // }
}