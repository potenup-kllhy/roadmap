package com.kllhy.roadmap.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApplicationEventPublisher publisher;

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex, HttpServletRequest req) {
        IErrorCode code = ex.getErrorCode();
        if (code == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ErrorResponse(
                                    Instant.now(),
                                    500,
                                    "Internal Server Error",
                                    "INTERNAL_ERROR",
                                    Optional.ofNullable(ex.getMessage())
                                            .orElse("Unexpected domain error"),
                                    req.getRequestURI()));
        }

        HttpStatus http = HttpStatusMapper.toSpringStatus(code.getHttpStatus());
        return ResponseEntity.status(http).body(ErrorResponse.of(http, code, req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus http = HttpStatus.BAD_REQUEST;
        String message =
                ex.getBindingResult().getFieldErrors().stream()
                        .findFirst()
                        .map(err -> err.getField() + " " + err.getDefaultMessage())
                        .orElse("Validation failed");
        var body =
                new ErrorResponse(
                        Instant.now(),
                        http.value(),
                        http.getReasonPhrase(),
                        "VALIDATION_ERROR",
                        message,
                        request.getRequestURI());
        return ResponseEntity.status(http).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        HttpStatus http = HttpStatus.INTERNAL_SERVER_ERROR;
        var body =
                new ErrorResponse(
                        Instant.now(),
                        http.value(),
                        http.getReasonPhrase(),
                        "INTERNAL_ERROR",
                        "Unexpected error",
                        request.getRequestURI());
        return ResponseEntity.status(http).body(body);
    }
}
