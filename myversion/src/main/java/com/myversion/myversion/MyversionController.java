package com.myversion.myversion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/myversion")
public class MyversionController {
    
    @GetMapping("/compare")
    public String showDesignForm(){
        return "design";
    }

    private FlaskProperties flaskProperties;

    public FlaskProperties FlaskController(FlaskProperties flaskProperties) {
        this.flaskProperties = flaskProperties;
        return this.flaskProperties;
    }
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/upload")
    public String getFlaskUrl() {
        return flaskProperties.getUrl();
    }

    public void doNothiing(){
        return ;
    }

}
