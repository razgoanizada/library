package raz.projects.library.dto.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdate {

    @NotNull
    @Size(min = 15, max = 250, message = "Please enter a short description between 15 - 250 characters")
    private String description;

    @NotNull
    @Pattern(regexp = "\\d{2,5}", message = "Invalid value. Must be a number with 2 to 5 digits")
    private String bookcase;

    @NotNull
    private String bookCategoriesName;
}
