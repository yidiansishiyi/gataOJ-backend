package com.yidiansishiyi.gataoj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    private String url;
    private String accessKey;
    private String secretKey;
    
    @Bean
    public MinioClient getMinioClient() {
        MinioClient minioClient = MinioClient.builder().endpoint(url)
				.credentials(accessKey, secretKey).build();
        return minioClient;
    }
    
}
