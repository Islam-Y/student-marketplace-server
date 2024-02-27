package org.startit.objectservice.config;

import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.startit.objectservice.decorator.FileResponseDecorator;
import org.startit.objectservice.mapper.FileResponseMapper;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfig {
    @Value("${spring.minio.bucket}")
    private String bucketName;
    @Bean
    public MinioService getMinioService() {
        return new MinioService();
    }

    @Bean
    public MinioClient minioClient() {
        try {
            var client = new MinioClient(
                    "http://127.0.0.1:9000",
                    "fost",
                    "fostfost"
            );
            if (!client.bucketExists(bucketName))
                client.makeBucket(bucketName);
            return client;
        } catch (MinioException |
                 InvalidKeyException |
                 IOException | NoSuchAlgorithmException |
                 XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public MinioConfigurationProperties minioProperties() {
        return new MinioConfigurationProperties();
    }

    @Bean
    public FileResponseMapper fileResponseMapper() {
        return new FileResponseDecorator();
    }
}
