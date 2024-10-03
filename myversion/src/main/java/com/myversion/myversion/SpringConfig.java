package com.myversion.myversion;


import com.myversion.myversion.repository.SpringDataJpaSongRepository;
import com.myversion.myversion.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig{
    private final SpringDataJpaSongRepository songRepository;

    @Autowired
    public SpringConfig(SpringDataJpaSongRepository  songRepository) {
        this.songRepository = songRepository;
    }

    @Bean
    public Service songService(){
        return new Service(songRepository);
    }
}