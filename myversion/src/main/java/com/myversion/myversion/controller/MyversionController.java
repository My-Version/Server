package com.myversion.myversion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


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

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name){
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    
    static class Hello{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    

    @GetMapping("/upload")
    public String getFlaskUrl() {
        return flaskProperties.getUrl();
    }

}
