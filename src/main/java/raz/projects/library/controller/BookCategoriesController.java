package raz.projects.library.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import raz.projects.library.dto.pages.BookCategoriesPageDto;
import raz.projects.library.dto.request.BookCategoriesRequestDto;
import raz.projects.library.dto.response.BookCategoriesResponseDto;
import raz.projects.library.service.Book.BookCategoriesService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin')")
@RequestMapping("/api/v1/books-categories")
public class BookCategoriesController {

    private final BookCategoriesService bookCategoriesService;

    @GetMapping()
    public ResponseEntity<List<BookCategoriesResponseDto>> getCategories () {

        return ResponseEntity.ok(bookCategoriesService.getCategories());
    }

    @GetMapping ("/page")
    public ResponseEntity<BookCategoriesPageDto> getCategoriesPage (
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(value = "name", required = false) String name
    ) {

        return ResponseEntity.ok(bookCategoriesService.getCategoriesPage(
                pageNo, pageSize, sortBy, sortDir, name));
    }

    @PostMapping("/add")
    public ResponseEntity<BookCategoriesResponseDto> addCategory (
            @RequestBody @Valid BookCategoriesRequestDto dto, UriComponentsBuilder uriComponentsBuilder) {

        var responseDto = bookCategoriesService.addCategories(dto);

        var uri = uriComponentsBuilder.path("/books-categories/{id}").buildAndExpand(responseDto.getId()).toUri();

        return ResponseEntity.created(uri).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookCategoriesResponseDto> getCategoryById (@PathVariable @Valid @NotNull Long id) {

        return ResponseEntity.ok(bookCategoriesService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookCategoriesResponseDto> updateCategoryById (@PathVariable @Valid @NotNull Long id,
                                                               @Valid @RequestBody BookCategoriesRequestDto dto) {

        return ResponseEntity.accepted().body(bookCategoriesService.updateCategory(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookCategoriesResponseDto> deleteCategoryById (@PathVariable @Valid @NotNull Long id) {

        return ResponseEntity.accepted().body(bookCategoriesService.deleteCategoryById(id));

    }
}
