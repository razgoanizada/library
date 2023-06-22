package raz.projects.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raz.projects.library.entity.Permissions;

public interface PermissionsRepository extends JpaRepository<Permissions, Long> {

    Permissions findPermissionsByPermissionIgnoreCase (String permission);
}
