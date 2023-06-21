package raz.projects.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.CustomerType;

public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {

    CustomerType findCustomerTypeByNameIgnoreCase (String name);

}
