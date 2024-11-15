package com.myversion.myversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myversion.myversion.repository.MemberRepository;
import com.myversion.myversion.service.MemberService;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class SpringConfig{

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
     this.memberRepository = memberRepository;
    }
    @Bean
    public MemberService Service(){
     return new MemberService(memberRepository);
    }

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