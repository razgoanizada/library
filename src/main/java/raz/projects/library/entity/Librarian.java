package raz.projects.library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import raz.projects.library.enums.Permissions;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "librarians")
public class Librarian {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    private String userName;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private String tz;

    @NotNull
    private Set<Permissions> permission;
}
