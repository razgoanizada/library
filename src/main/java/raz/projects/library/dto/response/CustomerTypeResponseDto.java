package raz.projects.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTypeResponseDto {

    private Long id;
    private String name;
    private int days;
    private int amountBooks;
}
