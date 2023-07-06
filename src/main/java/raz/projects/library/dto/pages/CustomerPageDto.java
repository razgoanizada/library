package raz.projects.library.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.dto.response.CustomerResponseDto;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPageDto {

    private List<CustomerResponseDto> results;
    private int totalPages;
    private long totalCustomers;
    private boolean isFirst;
    private boolean isLast;
    private int pageNo;
    private int pageSize;
}