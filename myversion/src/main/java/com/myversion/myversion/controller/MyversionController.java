package com.myversion.myversion.controller;

import com.myversion.myversion.domain.Song;
import com.myversion.myversion.repository.JpaSongRepository;
import com.myversion.myversion.service.Service;
import com.myversion.myversion.repository.SpringDataJpaSongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class MyversionController {

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
    public String postScore(String song, String file){
        service.CompareSong(song, file);
        return "design";
    }

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

    @GetMapping("/downloadFile")
    public String DownloadFile(String key, String file_name){
        return "file";
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
