package raz.projects.library.service.Log;

import raz.projects.library.dto.pages.LogPageDto;
import raz.projects.library.entity.Log;

import java.util.List;

public interface LogService {

    void logLoginAttempt(String username, String ipAddress, boolean isLogin);
    List<Log> getLogs();
    LogPageDto getLogsPage(
            int pageNo, int pageSize, String sortBy, String sortDir,
            String userName, Boolean isLogin, String DateStart, String DateEnd);
}
