package raz.projects.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raz.projects.library.dto.pages.LogPageDto;
import raz.projects.library.entity.Log;
import raz.projects.library.service.Log.LogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin')")
@RequestMapping("/api/v1/logs")
public class LogController {

    private final LogService logService;

    @GetMapping
    public ResponseEntity<List<Log>> getLogs () {

        return ResponseEntity.ok(logService.getLogs());
    }

    @GetMapping("/page")
    public ResponseEntity<LogPageDto> getBorrowedPage (

            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "isLogin", required = false) Boolean isLogin,
            @RequestParam(value = "DateStart", required = false,
                    defaultValue = "#{T(java.time.LocalDate).now().minusDays(30)" +
                            ".format(T(java.time.format.DateTimeFormatter)" +
                            ".ofPattern('yyyy-MM-dd'))}")
            String DateStart,
            @RequestParam(value = "DateEnd", required = false,
                    defaultValue = "#{T(java.time.LocalDate).now()" +
                            ".format(T(java.time.format.DateTimeFormatter)" +
                            ".ofPattern('yyyy-MM-dd'))}")
            String DateEnd)

    {
        return ResponseEntity.ok(logService.getLogsPage(
                pageNo, pageSize, sortBy, sortDir, userName, isLogin, DateStart, DateEnd));

    }
}
