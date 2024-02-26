package org.startit.objectservice.config;

import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.startit.objectservice.decorator.FileResponseDecorator;
import org.startit.objectservice.mapper.FileResponseMapper;

@Configuration
public class MinioConfig {
    @Bean
    public MinioService getMinioService() {
        return new MinioService();
    }

    @Bean
    public MinioClient minioClient() {
        try {
            return new MinioClient(
                    "http://127.0.0.1:9000",
                    "fost",
                    "fostfost"
            );
        } catch (InvalidEndpointException | InvalidPortException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public MinioConfigurationProperties minioProperties() {
        var properties = new MinioConfigurationProperties();
        properties.setCreateBucket(true);
        properties.setUrl("http://127.0.0.1:9000");
        properties.setBucket("test");
        properties.setAccessKey("fost");
        properties.setSecretKey("fostfost");
        return new MinioConfigurationProperties();
    }

    @Bean
    public FileResponseMapper fileResponseMapper() {
        return new FileResponseDecorator();
    }
}
