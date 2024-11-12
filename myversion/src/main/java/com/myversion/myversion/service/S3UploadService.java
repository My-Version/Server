package com.myversion.myversion.service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

@Service
public class S3UploadService {
    private final S3Client s3Client;

    @Autowired
    public S3UploadService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile multipartFile, String rawBucketName, String key) throws IOException {
        File file = convertToFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile to File conversion failed"));
        return uploadFile(file, rawBucketName, key);
    }

    public String uploadFile(File file, String rawBucketName, String key) {
        String bucketName = "my-version-"+rawBucketName+"-list";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // 파일을 S3로 업로드
        PutObjectResponse response = s3Client.putObject(
                putObjectRequest,
                RequestBody.fromFile(file)
        );

        // 업로드한 파일의 URL 생성
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();
    }

    // MultipartFile을 File로 변환
    private Optional<File> convertToFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return Optional.empty();
        }

        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(convFile);
        return Optional.of(convFile);
    }
}
