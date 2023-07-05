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
@Table(name = "customer_type",
        uniqueConstraints = @UniqueConstraint(columnNames = {"days", "amount_books"}))
public class CustomerType {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int days;

    @NotNull
    private int amountBooks;
}
