package raz.projects.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Book;


public interface BookRepository extends JpaRepository<Book, Long> {



}
