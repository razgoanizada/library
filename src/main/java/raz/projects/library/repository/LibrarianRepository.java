package raz.projects.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Librarian;

import java.util.Optional;

public interface LibrarianRepository extends JpaRepository<Librarian, Long> {

    Optional<Librarian> findLibrarianByEmailIgnoreCase (String email);
    Optional<Librarian> findLibrarianByUserNameIgnoreCase (String userName);
    Optional<Librarian> findLibrarianByTzIgnoreCase (String tz);

    Page<Librarian> findAll(Specification<Librarian> specification, Pageable pageable);
}
