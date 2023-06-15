package raz.projects.library.service;

import raz.projects.library.dto.pages.LibrarianPageDto;
import raz.projects.library.dto.request.LibrarianRequestDto;
import raz.projects.library.dto.response.LibrarianResponseDto;

import java.util.List;

public interface LibrarianService {

    List<LibrarianResponseDto> getLibrarians ();
    LibrarianPageDto getLibrariansPage (int pageNo, int pageSize, String sortBy, String sortDir);
    LibrarianResponseDto addLibrarian (LibrarianRequestDto dto);
    LibrarianResponseDto getLibrarianById(long id);
    LibrarianResponseDto deleteLibrarianById (Long id);
}
