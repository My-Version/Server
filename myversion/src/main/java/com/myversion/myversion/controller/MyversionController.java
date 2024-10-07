package com.myversion.myversion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class MyversionController {
    
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
    public String downloadFile(@PathVariable String fileName){
        return fileName;
    }

    // @GetMapping("/download/{fileName}")
    // public ResponseEntity<byte[]> getImage(@PathVariable Long fileName){
    //     FileInfoDto info = this.fileService.getImageInfoById(fileName);
    //     Path imagePath = Paths.get(info.getFilePath());
    //     String mimeType = info.getFileType();

    //     byte[] imageBytes = this.fileService.getImageFile(imagePath);

    //     return ResponseEntity.status(HttpStatus.OK)
    //             .contentType(MediaType.valueOf(mimeType))  // 이미지 타입에 맞게 설정
    //             .body(imageBytes);
    // }
    
}
