package raz.projects.library.dto.pages;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.dto.response.BookResponseDto;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookPageDto {

    private List<BookResponseDto> results;
    private int totalPages;
    private long totalBooks;
    private boolean isFirst;
    private boolean isLast;
    private int pageNo;
    private int pageSize;
}
