package raz.projects.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.BookCategories;

public interface BookCategoriesRepository extends JpaRepository<BookCategories, Long> {

    Page<BookCategories> findAll(Specification<BookCategories> specification, Pageable pageable);
}
