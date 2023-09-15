package raz.projects.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowResponseDto {

    private Long id;
    private Long customerId;
    private Long bookId;
    private LocalDate borrowingDate;
    private LocalDate returnDate;
    private LocalDate returnedOn;
    private boolean returnBook;
    private String addedByUserName;
}
