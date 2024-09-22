package com.myversion.myversion.controller;

import com.myversion.myversion.RequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotation.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "FlaskController", description = "Flas 관련 기능 컨트롤러")
public class FlaskController {
    private final FlaskService flaskService;

    @PostMapping("/flask")
    public String sendToFlask(@RequestBody RequestSendToFlaskDto dto) throws JsonProcessingException {
        return flaskService.sendToFlask(dto);
    }
}
