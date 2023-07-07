package raz.projects.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "books",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "author", "description"})}
        )
public class Book {

    @GeneratedValue
    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String author;

    @NotNull
    private String publishYear;

    @NotNull
    private String description;

    @NotNull
    private String bookcase;

    @ManyToOne
    @JoinColumn(name = "book_category_id")
    private BookCategories bookCategories;

    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian addedBy;

    @CreationTimestamp
    private Date creationDate;


    @OneToMany(mappedBy = "book")
    private List<Borrow> borrows;

}
