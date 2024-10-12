package com.myversion.myversion;


import com.myversion.myversion.repository.SpringDataJpaSongRepository;
import com.myversion.myversion.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

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

    //S3Client 빈 등록
    @Bean
    public S3Client s3Client(){
        return S3Client.builder()
                .region(Region.of("my_region")) // AWS 리전 설정
                .build();
    }
}