package raz.projects.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.CustomerType;

public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {

    CustomerType findCustomerTypeByNameIgnoreCase (String name);
    Page<CustomerType> findAll(Specification<CustomerType> specification, Pageable pageable);

}