package com.myversion.myversion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class SpringConfig{
    
    // @Autowired
    // public SpringConfig(Repository Repository) {
    //     this.Repository = Repository;
    // }

    // @Bean
    // public Service Service(){
    //     return new Service(SpringDataJpaRepository);
    // }

    @Bean
    public S3Client s3Client() {
        Dotenv dotenv = Dotenv.load();
        String accessKey = dotenv.get("AWS_ACCESS_KEY_ID");
        String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
        return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .region(Region.AP_NORTHEAST_2)
            .build();
    }
}