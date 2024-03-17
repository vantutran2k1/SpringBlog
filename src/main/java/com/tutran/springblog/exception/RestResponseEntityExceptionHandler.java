package com.tutran.springblog.exception;

import com.tutran.springblog.payload.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<ApiResponse> handleEntityNotFound(RuntimeException ex) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ApiResponse> buildResponseEntity(final RuntimeException ex, final HttpStatus status) {
        return new ResponseEntity<>(
                ApiResponse.builder().error(ex.getLocalizedMessage()).build(),
                new HttpHeaders(),
                status
        );
    }
}