package raz.projects.library.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import raz.projects.library.dto.pages.BookPageDto;
import raz.projects.library.dto.request.BookRequestDto;
import raz.projects.library.dto.response.BookResponseDto;
import raz.projects.library.dto.update.BookUpdateLocation;
import raz.projects.library.service.BookService;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/online-library/books")
public class BookController {

    private final BookService bookService;


    @PreAuthorize("hasAuthority(T(raz.projects.library.enums.Permissions).simple)")
    @GetMapping()
    public ResponseEntity<List<BookResponseDto>> getBooks () {

        return ResponseEntity.ok(bookService.getBooks());
    }

    @PreAuthorize("hasAuthority(T(raz.projects.library.enums.Permissions).simple)")
    @GetMapping ("/page")
    public ResponseEntity<BookPageDto> getBooksPage (
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {

        return ResponseEntity.ok(bookService.getBooksPage(pageNo, pageSize, sortBy, sortDir));
    }

    @PreAuthorize("hasAuthority(T(raz.projects.library.enums.Permissions).pro)")
    @PostMapping("/add")
    public ResponseEntity<BookResponseDto> addBook (@RequestBody @Valid BookRequestDto dto, UriComponentsBuilder uriComponentsBuilder) {

        var responseDto = bookService.addBook(dto);

        var uri = uriComponentsBuilder.path("/books/{id}").buildAndExpand(responseDto.getId()).toUri();

        return ResponseEntity.created(uri).body(responseDto);
    }

    @PreAuthorize("hasAuthority(T(raz.projects.library.enums.Permissions).simple)")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById (@PathVariable @Valid @NotNull Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PreAuthorize("hasAuthority(T(raz.projects.library.enums.Permissions).pro)")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBookLocation (@PathVariable @Valid @NotNull Long id,
                                                               @Valid @RequestBody BookUpdateLocation dto) {
        return ResponseEntity.accepted().body(bookService.updateBookLocation(dto, id));
    }

    @PreAuthorize("hasAuthority(T(raz.projects.library.enums.Permissions).pro)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBookById (@PathVariable @Valid @NotNull Long id) {
        return ResponseEntity.accepted().body(bookService.deleteBookById(id));

    }

}
