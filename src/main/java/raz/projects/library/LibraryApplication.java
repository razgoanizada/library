package raz.projects.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import raz.projects.library.config.LibraryJWTConfig;

@SpringBootApplication()
@EnableConfigurationProperties(LibraryJWTConfig.class)
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
        System.out.println("test");
    }
}
