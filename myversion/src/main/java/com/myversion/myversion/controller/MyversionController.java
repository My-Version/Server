package com.myversion.myversion.controller;

import com.myversion.myversion.FlaskProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;


@Controller
public class MyversionController {
    
    @PostMapping("/upload")
    public String callPythonApi(@RequestBody String text) {
        String apiUrl = "http://IP주소 바꿔서 넣어줘/upload";
        
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(apiUrl, "{\"text\": \"" + text + "\"}", String.class);

        // result는 파이썬 API의 응답을 나타냅니다.
        return result;
    }

    @GetMapping("/compare")
    public String showDesignForm(){
        return "design";
    }

}
