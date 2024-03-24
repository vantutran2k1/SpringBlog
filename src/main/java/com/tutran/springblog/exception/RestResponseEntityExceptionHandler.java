package com.tutran.springblog.exception;

import com.tutran.springblog.payload.ApiError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<ApiError> handleEntityNotFound(RuntimeException ex) {
        return this.buildErrorResponseEntity(ex, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    protected ResponseEntity<ApiError> handleConflict(RuntimeException ex) {
        return this.buildErrorResponseEntity(ex, null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            PropertyReferenceException.class,
            CommentNotBelongingToPostException.class,
            UserExistedException.class,
            JwtTokenException.class
    })
    protected ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        return this.buildErrorResponseEntity(ex, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        return this.buildErrorResponseEntity(ex, errors, HttpStatus.BAD_REQUEST);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    errors.put(fieldName, message);
                });

        ApiError apiError = ApiError.builder()
                .error(errors)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    private ResponseEntity<ApiError> buildErrorResponseEntity(
            final RuntimeException ex,
            final Object error,
            final HttpStatus status
    ) {
        String errorMessage = ex.getLocalizedMessage();
        ApiError apiError;
        if (error != null) {
            apiError = ApiError.builder().message(errorMessage).error(error).status(status).build();
        } else {
            apiError = ApiError.builder().message(errorMessage).status(status).build();
        }

        return new ResponseEntity<>(apiError, status);
    }
}