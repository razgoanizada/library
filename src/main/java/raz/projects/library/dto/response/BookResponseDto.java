package raz.projects.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    private Long id;
    private String name;
    private String author;
    private String publishYear;
    private String description;
    private String bookcase;
    private String bookCategoriesName;
    private String addedByUserName;
    private Date creationDate;
}
