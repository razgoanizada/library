package raz.projects.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findAll(Specification<Log> specification, Pageable pageable);
}
