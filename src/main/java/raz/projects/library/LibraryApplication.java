package raz.projects.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication()
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);

    }


    // TODO: 06/06/2023 add error 404 from general errors
    // TODO: 13/06/2023 add role per user
    // TODO: 13/06/2023  disabled security in test

}
