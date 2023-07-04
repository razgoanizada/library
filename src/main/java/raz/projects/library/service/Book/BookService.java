package raz.projects.library.service.Book;

import org.springframework.security.core.Authentication;
import raz.projects.library.dto.pages.BookPageDto;
import raz.projects.library.dto.request.BookRequestDto;
import raz.projects.library.dto.response.BookResponseDto;
import raz.projects.library.dto.update.BookUpdate;

import java.util.List;

public interface BookService {

     List<BookResponseDto> getBooks ();

    BookPageDto getBooksPage(
            int pageNo, int pageSize, String sortBy, String sortDir,
            String name, String author, String publishYear, String bookcase, String bookCategories, String addedBy);

    BookResponseDto addBook(BookRequestDto dto, Authentication authentication);

    BookResponseDto getBookById(long id);

    BookResponseDto updateBook(BookUpdate dto, Long id);

    BookResponseDto deleteBookById (Long id);

}
