package raz.projects.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Librarian;

import java.util.Optional;

public interface LibrarianRepository extends JpaRepository<Librarian, Long> {

    Optional<Librarian> findLibrarianByEmailIgnoreCase (String email);
    Optional<Librarian> findLibrarianByUserNameIgnoreCase (String userName);
    Optional<Librarian> findLibrarianByTzIgnoreCase (String tz);
    Boolean existsUserByUserNameIgnoreCase(String userName);
    Boolean existsUserByEmailIgnoreCase(String userName);
}
