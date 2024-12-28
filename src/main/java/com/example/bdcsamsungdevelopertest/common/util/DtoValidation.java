package com.example.bdcsamsungdevelopertest.common.util;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;

public class DtoValidation {

    public static void validateBlank(String value) {
        if(value.isBlank()) throw new BadRequestException("값이 비어 있습니다.");
    }
}
