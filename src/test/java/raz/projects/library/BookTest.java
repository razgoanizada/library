package raz.projects.library;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raz.projects.library.dto.request.BookRequestDto;
import raz.projects.library.dto.response.BookResponseDto;
import raz.projects.library.dto.update.BookUpdateLocation;
import raz.projects.library.repository.BookRepository;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    BookRepository bookRepository;

    private static final String getBooks = "/online-library/books";
    private static final String addBook = getBooks + "/add";

    @BeforeEach
    public void cleanUp() {
        bookRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void getBooks () {

        var response =  restTemplate.getForEntity(getBooks, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBooksPage () {

        var response =  restTemplate.getForEntity(getBooks + "/page", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addBook_httpStatus () {
        ResponseEntity<BookResponseDto> res = getBookResponseDtoResponseEntity();

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void addBookToDB () {
        BookRequestDto book = createBook();

        restTemplate.postForEntity(addBook, book, BookResponseDto.class);
        assertThat(bookRepository.count()).isEqualTo(1L);
    }

    @Test
    void addBook_Unique() {
        BookRequestDto book1 = createBook();
        restTemplate.postForEntity(addBook, book1, BookResponseDto.class);

        ResponseEntity<BookResponseDto> res = getBookResponseDtoResponseEntity();
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void addBook_NameHasNull() {
        BookRequestDto book = createBook();
        book.setName(null);
        addBook_BadRequest(book);
    }

    @Test
    void addBook_NameHas1Characters () {
        BookRequestDto book = createBook();
        book.setName("1");
        addBook_BadRequest(book);

    }

    @Test
    void addBook_AuthorHasNull() {
        BookRequestDto book = createBook();
        book.setAuthor(null);
        addBook_BadRequest(book);
    }

    @Test
    void addBook_AuthorHas1Characters () {
        BookRequestDto book = createBook();
        book.setAuthor("r");
        addBook_BadRequest(book);

    }

    @Test
    void addBook_LocationHasNull() {
        BookRequestDto book = createBook();
        book.setLocation(null);
        addBook_BadRequest(book);
    }

    @Test
    void addBook_LocationHas1Characters() {
        BookRequestDto book = createBook();
        book.setLocation("a");
        addBook_BadRequest(book);
    }

    @Test
    void getBookById_OK() {
        ResponseEntity<BookResponseDto> res = getBookResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId();

        var response = restTemplate.getForEntity(getBooks + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBookById_NotFound() {
        ResponseEntity<BookResponseDto> res = getBookResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId() + 1;

        var response = restTemplate.getForEntity(getBooks + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    

    @Test
    void updateBookById_Accepted() {
        ResponseEntity<BookResponseDto> res = getBookResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId();

        BookUpdateLocation bookUpdateLocation = updateBook();

        var bookEntity = new HttpEntity<>(bookUpdateLocation);

        var response = restTemplate.exchange(getBooks + "/" + id, HttpMethod.PUT, bookEntity, BookResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    void updateBookById_BadRequest() {
        ResponseEntity<BookResponseDto> res = getBookResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId() + 1;

        BookUpdateLocation bookUpdateLocation = updateBook();

        var bookEntity = new HttpEntity<>(bookUpdateLocation);

        var response = restTemplate.exchange(getBooks + "/" + id, HttpMethod.PUT, bookEntity, BookResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteBookById_Accepted() {
        ResponseEntity<BookResponseDto> res = getBookResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId();

        var response = restTemplate.exchange(getBooks + "/" + id, HttpMethod.DELETE, HttpEntity.EMPTY, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    void deleteBookById_BadRequest() {
        ResponseEntity<BookResponseDto> res = getBookResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId() + 1;

        var response = restTemplate.exchange(getBooks + "/" + id, HttpMethod.DELETE, HttpEntity.EMPTY, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ********************************************************
    // help method
    //******************************************************

    private void addBook_BadRequest(BookRequestDto book) {
        var res = restTemplate.postForEntity(addBook, book, BookResponseDto.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<BookResponseDto> getBookResponseDtoResponseEntity() {
        BookRequestDto book = createBook();
        return restTemplate.postForEntity(addBook, book, BookResponseDto.class);
    }
    
    private BookRequestDto createBook () {
        return BookRequestDto.builder()
                .name("harry")
                .author("raz")
                .location("floor -1")
                .build();
    }

    private BookUpdateLocation updateBook () {
        return BookUpdateLocation.builder()
                .location("floor 2")
                .build();
    }

}


