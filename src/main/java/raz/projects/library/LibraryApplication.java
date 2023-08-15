package raz.projects.library;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import raz.projects.library.config.LibraryJWTConfig;
import raz.projects.library.entity.BookCategories;
import raz.projects.library.entity.CustomerType;
import raz.projects.library.entity.Librarian;
import raz.projects.library.entity.Permissions;
import raz.projects.library.enums.Gender;
import raz.projects.library.repository.BookCategoriesRepository;
import raz.projects.library.repository.CustomerTypeRepository;
import raz.projects.library.repository.LibrarianRepository;
import raz.projects.library.repository.PermissionsRepository;

import java.time.LocalDate;

@SpringBootApplication()
@RequiredArgsConstructor
@EnableConfigurationProperties(LibraryJWTConfig.class)
public class LibraryApplication  implements CommandLineRunner {

    private final PermissionsRepository permissionsRepository;
    private final LibrarianRepository librarianRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerTypeRepository customerTypeRepository;
    private final BookCategoriesRepository bookCategoriesRepository;

    public static void main(String[] args) {

        SpringApplication.run(LibraryApplication.class, args);
    }

    @Override
    @Transactional
    public void run (String... args) {

        if (permissionsRepository.findAll().size() == 0) {

            permissionsRepository.save(new Permissions(1L, "admin",
                    "https://images.pexels.com/photos/13420005/pexels-photo-13420005.jpeg?auto=compress&cs=tinysrgb&w=1600"));
            permissionsRepository.save(new Permissions(2L, "pro",
                    "https://images.pexels.com/photos/9572566/pexels-photo-9572566.jpeg?auto=compress&cs=tinysrgb&w=1600"));
            permissionsRepository.save(new Permissions(3L, "simple",
                    "https://media.istockphoto.com/id/910852368/photo/student-searching-books.jpg?b=1&s=612x612&w=0&k=20&c=iZ85GHZWIwM1aaHG5vEnDCgZbpfxvlCvM2O0E8os0k8="));
        }

        if (librarianRepository.findAll().size() == 0) {

            librarianRepository.save(Librarian.builder()
                    .id(1L)
                    .firstName("admin")
                    .lastName("admin")
                    .email("admin@gmail.com")
                    .userName("admin")
                    .password(passwordEncoder.encode("Password1!"))
                    .permission(permissionsRepository.findPermissionsByPermissionIgnoreCase("admin"))
                    .phone("0521234567")
                    .tz("999572639")
                    .address("New York City")
                    .gender(Gender.male)
                    .DateOfBirth(LocalDate.of(2000, 1, 1))
                    .build());
        }

        if (customerTypeRepository.findAll().size() == 0) {

            customerTypeRepository.save(new CustomerType(1L, "vip", 180, 10));
            customerTypeRepository.save(new CustomerType(2L, "pro", 60, 5));
            customerTypeRepository.save(new CustomerType(3L, "simple", 30, 2));
        }

        if (bookCategoriesRepository.findAll().size() == 0) {

            bookCategoriesRepository.save(new BookCategories(1L, "drama"));
            bookCategoriesRepository.save(new BookCategories(2L, "action"));
        }

    }
}
