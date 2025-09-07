package org.projects.customerservice.api.handler;


import jakarta.persistence.EntityNotFoundException;

import org.projects.customerservice.api.exception.BusinessException;
import org.projects.customerservice.api.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class UserExceptionHandler {

    

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(final BusinessException ex) {
        final ErrorResponse body = ErrorResponse.builder()
                                                .code(ex.getErrorCode()
                                                        .getCode())
                                                .message(ex.getMessage())
                                                .build();
        log.info("Business Exception: {}", body);
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getErrorCode()
                                       .getStatus() != null ? ex.getErrorCode()
                                                                .getStatus() : BAD_REQUEST)
                             .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException exp) {
        final List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        exp.getBindingResult()
           .getAllErrors()
           .forEach(error -> {
               final String fieldName = ((FieldError) error).getField();
               final String errorCode = error.getDefaultMessage();
               errors.add(ErrorResponse.ValidationError.builder()
                                                       .field(fieldName)
                                                       .code(errorCode)
                                                       .message(errorCode)
                                                       .build());
           });
        final ErrorResponse errorResponse = ErrorResponse.builder()
                                                         .validationErrors(errors)
                                                         .build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(final BadCredentialsException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                                                    .message(ErrorCode.BAD_CREDENTIALS.getDefaultMessage())
                                                    .code(ErrorCode.BAD_CREDENTIALS.getCode())
                                                    .build();
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final EntityNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                                                         .code("TBD")
                                                         .message(exception.getMessage())
                                                         .build();
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final UsernameNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                                                    .code(ErrorCode.USERNAME_NOT_FOUND.getCode())
                                                    .message(ErrorCode.USERNAME_NOT_FOUND.getDefaultMessage())
                                                    .build();
        return new ResponseEntity<>(response,
                                    NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(final AuthorizationDeniedException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                                                    .message("You are not authorized to perform this operation")
                                                    .build();
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                                                    .code(ErrorCode.INTERNAL_EXCEPTION.getCode())
                                                    .message(ErrorCode.INTERNAL_EXCEPTION.getDefaultMessage())
                                                    .build();
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }
}
