package raz.projects.library.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.entity.Log;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogPageDto {

    private List<Log> results;
    private int totalPages;
    private long totalLogs;
    private boolean isFirst;
    private boolean isLast;
    private int pageNo;
    private int pageSize;
}
