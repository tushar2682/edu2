package com.countryedu.empapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ) {
        List<ErrorResponse.ValidationError> validationErrors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.add(new ErrorResponse.ValidationError(
                    error.getField(),
                    error.getRejectedValue(),
                    error.getDefaultMessage()
            ));
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("https://api.countryedu.com/errors/validation-failed")
                .title("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("Input validation failed for " + validationErrors.size() + " field(s)")
                .instance(request.getRequestURI())
                .errorCode("ERR_INVALID_INPUT")
                .timestamp(LocalDateTime.now())
                .validationErrors(validationErrors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("https://api.countryedu.com/errors/resource-not-found")
                .title("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .errorCode("ERR_NOT_FOUND")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(
            BusinessRuleException ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("https://api.countryedu.com/errors/business-rule-violation")
                .title("Business Rule Violation")
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .errorCode("ERR_BUSINESS_RULE")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailure(
            ObjectOptimisticLockingFailureException ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("https://api.countryedu.com/errors/optimistic-lock-conflict")
                .title("Resource Conflict")
                .status(HttpStatus.CONFLICT.value())
                .detail("The record was modified or updated concurrently by another transaction. Please reload and re-apply changes.")
                .instance(request.getRequestURI())
                .errorCode("ERR_CONCURRENT_UPDATE_CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("https://api.countryedu.com/errors/invalid-credentials")
                .title("Authentication Failed")
                .status(HttpStatus.UNAUTHORIZED.value())
                .detail("Invalid username or password")
                .instance(request.getRequestURI())
                .errorCode("ERR_UNAUTHORIZED")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("https://api.countryedu.com/errors/access-denied")
                .title("Access Denied")
                .status(HttpStatus.FORBIDDEN.value())
                .detail("You do not have permission to perform this action.")
                .instance(request.getRequestURI())
                .errorCode("ERR_FORBIDDEN")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("https://api.countryedu.com/errors/internal-server-error")
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail(ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred")
                .instance(request.getRequestURI())
                .errorCode("ERR_INTERNAL_SERVER")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
