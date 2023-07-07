package raz.projects.library.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.dto.response.BorrowResponseDto;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowPageDto {

    private List<BorrowResponseDto> results;
    private int totalPages;
    private long totalBorrowed;
    private boolean isFirst;
    private boolean isLast;
    private int pageNo;
    private int pageSize;
}
