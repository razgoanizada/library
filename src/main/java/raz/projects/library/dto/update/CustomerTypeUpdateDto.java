package raz.projects.library.dto.update;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public class CustomerTypeUpdateDto {

    @NotNull
    @Max(180)
    private int days;

    @NotNull
    @Max(10)
    private int amountBooks;
}
