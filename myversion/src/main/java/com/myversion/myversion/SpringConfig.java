package com.myversion.myversion;

import com.myversion.myversion.repository.SongRepository;
import com.myversion.myversion.repository.SpringDataJpaSongRepository;
import com.myversion.myversion.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig{
    private final SpringDataJpaSongRepository songRepository;

    @Autowired
    public SpringConfig(SpringDataJpaSongRepository  songRepository) {
        this.songRepository = songRepository;
    }

    @Bean
    public SongService songService(){
        return new SongService(songRepository);
    }
}