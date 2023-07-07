package raz.projects.library.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import raz.projects.library.dto.pages.BorrowPageDto;
import raz.projects.library.dto.request.BorrowRequestDto;
import raz.projects.library.dto.response.BorrowResponseDto;
import raz.projects.library.dto.update.BorrowExtraTime;
import raz.projects.library.service.Borrow.BorrowService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/online-library/borrow")
public class BorrowController {

    private final BorrowService borrowService;


    @GetMapping()
    public ResponseEntity<List<BorrowResponseDto>> getBorrowed () {

        return ResponseEntity.ok(borrowService.getBorrowed());
    }

    @GetMapping("/page")
    public ResponseEntity<BorrowPageDto> getBooksPage (

            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(value = "customerId", required = false) String customerId,
            @RequestParam(value = "bookId", required = false) String bookId,
            @RequestParam(value = "addedBy", required = false) String addedBy,
            @RequestParam(value = "returnBook", required = false, defaultValue = "false") Boolean returnBook,
            @RequestParam(value = "borrowingDateStart", required = false,
                    defaultValue = "#{T(java.time.LocalDate).now().minusDays(365)" +
                            ".format(T(java.time.format.DateTimeFormatter)" +
                            ".ofPattern('yyyy-MM-dd'))}")
                                String borrowingDateStart,
            @RequestParam(value = "borrowingDateEnd", required = false,
                    defaultValue = "#{T(java.time.LocalDate).now()" +
                            ".format(T(java.time.format.DateTimeFormatter)" +
                            ".ofPattern('yyyy-MM-dd'))}")
                                String borrowingDateEnd,
            @RequestParam(value = "returnDateStart", required = false,
                    defaultValue = "#{T(java.time.LocalDate).now().minusDays(365)" +
                            ".format(T(java.time.format.DateTimeFormatter)" +
                            ".ofPattern('yyyy-MM-dd'))}")
                                String returnDateStart,
            @RequestParam(value = "returnDateEnd", required = false,
                    defaultValue =  "#{T(java.time.LocalDate).now().plusDays(365)" +
                            ".format(T(java.time.format.DateTimeFormatter)" +
                            ".ofPattern('yyyy-MM-dd'))}")
                                String returnDateEnd)

    {
        return ResponseEntity.ok(borrowService.getBorrowedPage(
                pageNo, pageSize, sortBy, sortDir,
                customerId, bookId, addedBy, returnBook,
                borrowingDateStart, borrowingDateEnd,
                returnDateStart, returnDateEnd));

    }

    @PostMapping("/add")
    public ResponseEntity<BorrowResponseDto> addBorrow (
            @RequestBody @Valid BorrowRequestDto dto,
            UriComponentsBuilder uriComponentsBuilder,
            Authentication authentication) {

        var responseDto = borrowService.addBorrow(dto, authentication);

        var uri = uriComponentsBuilder.path("/borrow/{id}").buildAndExpand(responseDto.getId()).toUri();

        return ResponseEntity.created(uri).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowResponseDto> getBorrowById (@PathVariable @Valid @NotNull Long id) {

        return ResponseEntity.ok(borrowService.getBorrowById(id));
    }

    @PutMapping("/extra-time/{id}")
    public ResponseEntity<BorrowResponseDto> extraTime (@Valid @RequestBody BorrowExtraTime dto,
                                                        @PathVariable @Valid @NotNull Long id) {

        return ResponseEntity.accepted().body(borrowService.extraTime(dto, id));
    }

    @PutMapping("/return-book/{id}")
    public ResponseEntity<BorrowResponseDto> returnBook (@PathVariable @Valid @NotNull Long id) {

        return ResponseEntity.accepted().body(borrowService.returnBook(id));
    }

}
