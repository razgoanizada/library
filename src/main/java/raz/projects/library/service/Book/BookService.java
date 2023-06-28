package raz.projects.library.service.Book;

import raz.projects.library.dto.pages.BookPageDto;
import raz.projects.library.dto.request.BookRequestDto;
import raz.projects.library.dto.response.BookResponseDto;
import raz.projects.library.dto.update.BookUpdateLocation;

import java.util.List;

public interface BookService {

     List<BookResponseDto> getBooks ();

    BookPageDto getBooksPage(int pageNo, int pageSize, String sortBy, String sortDir);

    BookResponseDto addBook (BookRequestDto dto);
     BookResponseDto getBookById(long id);
     BookResponseDto updateBookLocation (BookUpdateLocation dto, Long id);
     BookResponseDto deleteBookById (Long id);

}
