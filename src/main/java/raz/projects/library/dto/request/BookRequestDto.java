package raz.projects.library.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDto {

    @NotNull
    @Size(min = 2,  max = 15, message = "name is must be between 2 - 15 characters")
    private String name;

    @NotNull
    @Size(min = 2, max = 10, message = "name is must be between 2 - 10 characters")
    private String author;

    @NotNull
    @Pattern(regexp = "\\d{4}", message = "Invalid value. Must be a 4-digit number")
    private String publishYear;

    @NotNull
    @Size(min = 15, max = 250, message = "Please enter a short description between 15 - 250 characters")
    private String description;

    @NotNull
    @Pattern(regexp = "\\d{2,5}", message = "Invalid value. Must be a number with 2 to 5 digits")
    private String bookcase;

    @NotNull
    private String bookCategoriesName;
}
