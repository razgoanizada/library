package raz.projects.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Book;
import raz.projects.library.entity.Borrow;
import raz.projects.library.entity.Librarian;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    List<Borrow> findAllBorrowByCustomer_IdAndReturnBookIsFalse (Long id);
    List<Borrow> findAllBorrowByBook_IdAndReturnBookIsFalse (Long id);
    List<Borrow> findAllByBook (Book book);
    List<Borrow> findAllByAddedBy (Librarian librarian);
    Page<Borrow> findAll(Specification<Borrow> specification, Pageable pageable);

}
