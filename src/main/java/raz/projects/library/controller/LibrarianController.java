package raz.projects.library.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import raz.projects.library.dto.pages.LibrarianPageDto;
import raz.projects.library.dto.request.LibrarianRequestDto;
import raz.projects.library.dto.response.LibrarianResponseDto;
import raz.projects.library.service.LibrarianService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/librarians")
public class LibrarianController {

    private final LibrarianService librarianService;


    @PreAuthorize("hasAuthority(T(raz.projects.library.enums.Permissions).admin)")
    @GetMapping
    public ResponseEntity<List<LibrarianResponseDto>> getLibrarian () {

        return ResponseEntity.ok(librarianService.getLibrarians());
    }

    @GetMapping ("/page")
    public ResponseEntity<LibrarianPageDto> getLibrarianPage (
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {

        return ResponseEntity.ok(librarianService.getLibrariansPage(pageNo, pageSize, sortBy, sortDir));
    }


    @PreAuthorize("hasAuthority(T(raz.projects.library.enums.Permissions).admin)")
    @PostMapping("/add")
    public ResponseEntity<LibrarianResponseDto> addLibrarian (@RequestBody @Valid LibrarianRequestDto dto, UriComponentsBuilder uriComponentsBuilder) {

        var responseDto = librarianService.addLibrarian(dto);

        var uri = uriComponentsBuilder.path("/librarians/{id}").buildAndExpand(responseDto.getId()).toUri();

        return ResponseEntity.created(uri).body(responseDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<LibrarianResponseDto> getLibrarianById (@PathVariable @Valid @NotNull Long id) {
        return ResponseEntity.ok(librarianService.getLibrarianById(id));
    }

    // TODO 05/06/2023 add update

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLibrarianById (@PathVariable Long id) {
        return ResponseEntity.accepted().body(librarianService.deleteLibrarianById(id));
    }

}
