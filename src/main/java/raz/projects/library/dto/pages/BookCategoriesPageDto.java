package raz.projects.library.dto.pages;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.dto.response.BookCategoriesResponseDto;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoriesPageDto {

    private List<BookCategoriesResponseDto> results;
    private int totalPages;
    private long totalCategories;
    private boolean isFirst;
    private boolean isLast;
    private int pageNo;
    private int pageSize;
}
