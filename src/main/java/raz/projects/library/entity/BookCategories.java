package raz.projects.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_categories",
        uniqueConstraints =    @UniqueConstraint(columnNames = {"name"}))
public class BookCategories {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
}
