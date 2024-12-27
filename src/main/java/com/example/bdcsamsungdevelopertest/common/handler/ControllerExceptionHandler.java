package com.example.bdcsamsungdevelopertest.common.handler;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.common.exception.InternalServerException;
import com.example.bdcsamsungdevelopertest.common.exception.NotFoundException;
import com.example.bdcsamsungdevelopertest.common.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<CommonResponse> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(new CommonResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundException(BadRequestException e) {
        return new ResponseEntity<>(new CommonResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerException.class)
    protected ResponseEntity<CommonResponse> handleInternalServerException(BadRequestException e) {
        return new ResponseEntity<>(new CommonResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
