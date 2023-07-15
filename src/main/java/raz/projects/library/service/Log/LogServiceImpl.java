package raz.projects.library.service.Log;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.pages.LogPageDto;
import raz.projects.library.entity.Log;
import raz.projects.library.repository.LogRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final ModelMapper mapper;

    @Override
    public void logLoginAttempt(String username, String ipAddress, boolean isLogin) {

     var log = Log.builder()
              .username(username)
              .ipAddress(ipAddress)
              .isLogin(isLogin)
              .build();

      logRepository.save(log);
    }

    @Override
    public List<Log> getLogs() {

        return logRepository.findAll()
                .stream()
                .toList();
    }

    @Override
    public LogPageDto getLogsPage(
            int pageNo, int pageSize, String sortBy, String sortDir,
            String userName, Boolean isLogin, String DateStart, String DateEnd) {


        Specification<Log> specification = Specification.where(null);


        if (userName != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("username"), userName));
        }


        if (isLogin != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isLogin"), isLogin));
        }

        if (DateStart != null && DateEnd != null) {
            LocalDate startDate = LocalDate.parse(DateStart);
            LocalDate endDate = LocalDate.parse(DateEnd);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("loginDate"), startDate, endDate));
        }


        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<Log> page = logRepository.findAll(specification ,pageable);

        return LogPageDto.builder()
                .results(page.stream().map(log -> mapper.map(log, Log.class)).toList())
                .totalPages(page.getTotalPages())
                .totalLogs(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }

}
