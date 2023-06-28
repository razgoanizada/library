package raz.projects.library.service.Book;


import raz.projects.library.dto.pages.BookCategoriesPageDto;
import raz.projects.library.dto.request.BookCategoriesRequestDto;
import raz.projects.library.dto.response.BookCategoriesResponseDto;

import java.util.List;

public interface BookCategoriesService {

    List<BookCategoriesResponseDto> getCategories ();

    BookCategoriesPageDto getCategoriesPage(int pageNo, int pageSize, String sortBy, String sortDir, String name);

    BookCategoriesResponseDto addCategories (BookCategoriesRequestDto dto);
    BookCategoriesResponseDto getCategoryById(long id);
    BookCategoriesResponseDto updateCategory (BookCategoriesRequestDto dto, Long id);
    BookCategoriesResponseDto deleteCategoryById (Long id);
}
