package com.example.bdcsamsungdevelopertest.common.util;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableExtension {

    public static Pageable toPageable(
        Integer reqPage,
        Integer pageSize,
        String reqSortDir
    ) {
        if(pageSize < 1) throw new BadRequestException("요청 페지이 크기가 1보다 작을 수 없습니다.");
        Sort.Direction sortDir = Sort.Direction.DESC;
        if(reqSortDir.equals("asc")) sortDir = Sort.Direction.ASC;
        return PageRequest.of(
            reqPage,
            pageSize,
            sortDir,
            "createDt"  // fixed due to no further information about sorting
        );
    }
}
