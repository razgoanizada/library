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
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private String tz;

    private Gender gender;

    private String address;

    private LocalDate DateOfBirth;

    @NotNull
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "customer_type_id")
    private CustomerType customerType;

    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian addedBy;

    @CreationTimestamp
    private Date CreationDate;
}