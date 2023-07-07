package raz.projects.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrow")
public class Borrow {

    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDate borrowingDate;

    private LocalDate returnDate;

    private LocalDate retrievedOn;

    private boolean returnBook;

    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian addedBy;

}
