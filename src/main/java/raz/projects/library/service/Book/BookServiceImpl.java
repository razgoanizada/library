package raz.projects.library.service.Book;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.pages.BookPageDto;
import raz.projects.library.dto.request.BookRequestDto;
import raz.projects.library.dto.response.BookResponseDto;
import raz.projects.library.dto.update.BookUpdate;
import raz.projects.library.entity.Book;
import raz.projects.library.entity.BookCategories;
import raz.projects.library.entity.Borrow;
import raz.projects.library.errors.BadRequestException;
import raz.projects.library.errors.ResourceNotFoundException;
import raz.projects.library.repository.BookCategoriesRepository;
import raz.projects.library.repository.BookRepository;
import raz.projects.library.repository.BorrowRepository;
import raz.projects.library.repository.LibrarianRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final BookCategoriesRepository bookCategoriesRepository;
    private final LibrarianRepository librarianRepository;
    private final BorrowRepository borrowRepository;
    private final ModelMapper mapper;

    @Override
    public List<BookResponseDto> getBooks () {

        return bookRepository.findAll()
                .stream()
                .map(book -> mapper.map(book, BookResponseDto.class))
                .toList();
    }

    @Override
    public BookPageDto getBooksPage(
            int pageNo, int pageSize, String sortBy, String sortDir,
            String name, String author, String publishYear, String bookcase, String bookCategories, String addedBy) {

        Specification<Book> specification = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("name")), "%" + name.toLowerCase() + "%" ));
        }

        if (author != null && !author.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("author")), "%" + author.toLowerCase() + "%" ));
        }

        if (publishYear != null && !publishYear.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("publishYear")), "%" + publishYear.toLowerCase() + "%" ));
        }

        if (bookcase != null && !bookcase.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("bookcase")), "%" + bookcase.toLowerCase() + "%" ));
        }

        if (bookCategories != null && !bookCategories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("bookCategories"), bookCategories));
        }

        if (addedBy != null && !addedBy.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("addedBy"), addedBy));
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<Book> page = bookRepository.findAll(specification ,pageable);

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
    public BookResponseDto addBook(BookRequestDto dto, Authentication authentication) {

        BookCategories bookCategories =  bookCategoriesRepository
                .findBookCategoriesByNameIgnoreCase(dto.getBookCategoriesName());

        if (bookCategories == null) {
            throw new BadRequestException(
                    "add book - categories",
                    dto.getBookCategoriesName(),
                    "This categories doesn't exist in the library");
        }

        var librarian = librarianRepository.findLibrarianByUserNameIgnoreCase(authentication.getName()).orElseThrow();

        var book = mapper.map(dto, Book.class);

        book.setBookCategories(bookCategories);
        book.setAddedBy(librarian);

       try {
           bookRepository.save(book);
           var response =  mapper.map(book, BookResponseDto.class);

           response.setBookCategoriesName(book.getBookCategories().getName());
           response.setAddedByUserName(book.getAddedBy().getUserName());

           return response;

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
    public BookResponseDto updateBook(BookUpdate dto, Long id) {

        var book = bookRepository.findById(id).orElseThrow(
                () -> new BadRequestException("update book", id, "This book doesn't exist in the library")
        );

        BookCategories bookCategories =  bookCategoriesRepository
                .findBookCategoriesByNameIgnoreCase(dto.getBookCategoriesName());

        if (bookCategories == null) {
            throw new BadRequestException(
                    "update book - categories",
                    dto.getBookCategoriesName(),
                    "This categories doesn't exist in the library");
        }

       book.setDescription(dto.getDescription());
       book.setBookcase(dto.getBookcase());
       book.setBookCategories(bookCategories);

       bookRepository.save(book);
       var response = mapper.map(book, BookResponseDto.class);

       response.setBookCategoriesName(book.getBookCategories().getName());
       response.setAddedByUserName(book.getAddedBy().getUserName());

       return response;
    }

    @Override
    public BookResponseDto deleteBookById(Long id) {

        var exists = bookRepository.existsById(id);
        var book = bookRepository.findById(id).orElseThrow(
                () -> new BadRequestException("delete book", id, "This book doesn't exist in the library")
        );

        List<Borrow> borrows = borrowRepository.findAllByBook(book);

        if (!borrows.isEmpty())
            borrows.forEach(
                    borrow -> borrow.setBook(null)
            );

        if (exists)
            bookRepository.deleteById(id);

        book.setBorrows(null);

        return mapper.map(book, BookResponseDto.class);
    }



}
