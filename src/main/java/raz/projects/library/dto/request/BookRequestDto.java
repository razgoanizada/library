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
public class BookRequestDto {

    @NotNull
    @Size(min = 2,  max = 30, message = "name is must be between 2 - 30 characters")
    private String name;

    @NotNull
    @Size(min = 2, max = 30, message = "name is must be between 2 - 30 characters")
    private String author;

    @NotNull
    @Size(min = 2, max = 30, message = "location is must be between 2 - 30 characters")
    private String location;
}
