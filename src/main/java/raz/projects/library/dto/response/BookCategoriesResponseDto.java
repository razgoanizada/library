package raz.projects.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoriesResponseDto {

    private Long id;
    private String name;
}
