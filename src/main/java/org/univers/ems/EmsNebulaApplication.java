package org.univers.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class EmsNebulaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmsNebulaApplication.class, args);
    }

}
