package com.example.testypie;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TestyPieApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.properties,"
            + "classpath:aws.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestyPieApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}