package raz.projects.library.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoriesRequestDto {

    @NotNull
    @Size (min = 2, message = "The name must contain at least 2 characters")
    private String name;
}
