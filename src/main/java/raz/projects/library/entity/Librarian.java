package raz.projects.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Role> roles;
}
