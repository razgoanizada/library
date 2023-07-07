package raz.projects.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Book;
import raz.projects.library.entity.BookCategories;
import raz.projects.library.entity.Librarian;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByBookCategories (BookCategories bookCategories);
    List<Book> findAllByAddedBy (Librarian librarian);
    Page<Book> findAll(Specification<Book> specification, Pageable pageable);

}
