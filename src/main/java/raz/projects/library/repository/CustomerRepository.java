package raz.projects.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findCustomerByEmailIgnoreCase (String email);
    Optional<Customer> findCustomerByTzIgnoreCase (String tz);
    Page<Customer> findAll(Specification<Customer> specification, Pageable pageable);

}