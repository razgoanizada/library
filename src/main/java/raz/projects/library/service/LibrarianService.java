package raz.projects.library.service;

import raz.projects.library.dto.pages.LibrarianPageDto;
import raz.projects.library.dto.request.LibrarianRequestDto;
import raz.projects.library.dto.response.LibrarianResponseDto;
import raz.projects.library.dto.update.LibrarianChangePassword;

import java.util.List;

public interface LibrarianService {

    List<LibrarianResponseDto> getLibrarians ();
    LibrarianPageDto getLibrariansPage (int pageNo, int pageSize, String sortBy, String sortDir);
    LibrarianResponseDto addLibrarian (LibrarianRequestDto dto);
    LibrarianResponseDto getLibrarianById(Long id);
    LibrarianResponseDto deleteLibrarianById (Long id);
    LibrarianResponseDto librarianChangePassword(LibrarianChangePassword dto, Long id);
}
