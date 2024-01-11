package com.example.testypie;

import com.example.testypie.domain.util.S3Uploader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TestyPieApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.properties,"
            + "classpath:aws.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(S3Uploader.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}