package raz.projects.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
