package raz.projects.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import raz.projects.library.enums.Gender;
import java.sql.Date;
import java.time.LocalDate;


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
    private String firstName;

    @NotNull
    private String lastName;

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

    private Gender gender;

    private String address;

    private LocalDate DateOfBirth;

    @CreationTimestamp
    private Date CreationDate;

    @ManyToOne
    @JoinColumn(name = "permissions_permission")
    private Permissions permission;
}
