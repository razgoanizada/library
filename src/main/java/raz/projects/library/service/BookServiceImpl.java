package raz.projects.library.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.pages.BookPageDto;
import raz.projects.library.dto.request.BookRequestDto;
import raz.projects.library.dto.response.BookResponseDto;
import raz.projects.library.dto.update.BookUpdateLocation;
import raz.projects.library.entity.Book;
import raz.projects.library.errors.BadRequestException;
import raz.projects.library.errors.ResourceNotFoundException;
import raz.projects.library.repository.BookRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final ModelMapper mapper;

    @Override
    public List<BookResponseDto> getBooks () {

        return bookRepository.findAll()
                .stream()
                .map(book -> mapper.map(book, BookResponseDto.class))
                .toList();
    }

    @Override
    public BookPageDto getBooksPage(int pageNo, int pageSize, String sortBy, String sortDir) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<Book> page = bookRepository.findAll(pageable);

        return BookPageDto.builder()
                .results(page.stream().map(book -> mapper.map(book, BookResponseDto.class)).toList())
                .totalPages(page.getTotalPages())
                .totalBooks(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }


    @Override
    public BookResponseDto addBook(BookRequestDto dto) {

        var book = mapper.map(dto, Book.class);

       try {
           var savedBook = bookRepository.save(book);
           return mapper.map(savedBook, BookResponseDto.class);
       } catch (DataIntegrityViolationException exception) {
           throw new BadRequestException("add book", book.getId(), "This book is already in the library");
       }


    }
    @Override
    public BookResponseDto getBookById(long id) {

        var book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("get book" ,id, "This book doesn't exist in the library")
        );
        return mapper.map(book, BookResponseDto.class);
    }

    @Override
    public BookResponseDto updateBookLocation(BookUpdateLocation dto, Long id) {

        Book book = bookRepository.findById(id).orElseThrow(
                () -> new BadRequestException("update book", id, "This book doesn't exist in the library")
        );

        book.setLocation(dto.getLocation());

        var save = bookRepository.save(book);

        return mapper.map(save, BookResponseDto.class);
    }

    @Override
    public BookResponseDto deleteBookById(Long id) {

        var exists = bookRepository.existsById(id);
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new BadRequestException("delete book", id, "This book doesn't exist in the library")
        );

        if (exists)
            bookRepository.deleteById(id);

        return mapper.map(book, BookResponseDto.class);
    }



}
