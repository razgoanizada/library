package raz.projects.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    private long id;
    private String name;
    private String author;
    private String location;
    private Date CreationDate;
}
