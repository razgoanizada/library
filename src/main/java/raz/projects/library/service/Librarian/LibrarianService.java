package raz.projects.library.service.Librarian;

import raz.projects.library.dto.pages.LibrarianPageDto;
import raz.projects.library.dto.request.LibrarianRequestDto;
import raz.projects.library.dto.response.LibrarianResponseDto;
import raz.projects.library.dto.update.LibrarianChangePassword;
import raz.projects.library.dto.update.LibrarianUpdate;

import java.util.List;

public interface LibrarianService {

    List<LibrarianResponseDto> getLibrarians ();
    LibrarianPageDto getLibrariansPage (  int pageNo, int pageSize, String sortBy, String sortDir,
                                          String permission, String firstName, String lastName,
                                          String phone, String tz, String useName);
    LibrarianResponseDto addLibrarian (LibrarianRequestDto dto);
    LibrarianResponseDto getLibrarianById(Long id);
    LibrarianResponseDto updateLibrarianById (LibrarianUpdate dto, Long id);
    LibrarianResponseDto deleteLibrarianById (Long id);
    LibrarianResponseDto librarianChangePassword(LibrarianChangePassword dto, Long id);
}
