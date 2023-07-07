package raz.projects.library.service.Borrow;

import org.springframework.security.core.Authentication;
import raz.projects.library.dto.pages.BorrowPageDto;
import raz.projects.library.dto.request.BorrowRequestDto;
import raz.projects.library.dto.response.BorrowResponseDto;
import raz.projects.library.dto.update.BorrowExtraTime;

import java.util.List;

public interface BorrowService {

    List<BorrowResponseDto> getBorrowed ();
    BorrowPageDto getBorrowedPage (
            int pageNo, int pageSize, String sortBy, String sortDir,
            String customerId, String bookId, String addedBy, Boolean returnBook,
            String borrowingDateStart,  String borrowingDateEnd,
            String returnDateStart, String returnDateEnd);
    BorrowResponseDto addBorrow (BorrowRequestDto dto, Authentication authentication);
    BorrowResponseDto getBorrowById (Long id);
    BorrowResponseDto extraTime(BorrowExtraTime dto, Long id);
    BorrowResponseDto returnBook (Long id);
}
