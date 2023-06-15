package raz.projects.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "books",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "author"})}
        )
public class Book {

    @GeneratedValue
    @Id
    private Long id;

    @NotNull
    @Size(min = 2,  max = 30)
    private String name;

    @NotNull
    @Size(min = 2, max = 30)
    private String author;

    @NotNull
    @Size(min = 2, max = 30)
    private String location;

    @CreationTimestamp
    private Date CreationDate;

    // TODO: 05/06/2023 add category (enum)
}
