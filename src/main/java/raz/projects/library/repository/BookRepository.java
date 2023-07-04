package raz.projects.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Book;


public interface BookRepository extends JpaRepository<Book, Long> {


    Page<Book> findAll(Specification<Book> specification, Pageable pageable);

}
