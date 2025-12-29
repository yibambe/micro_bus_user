package com.pos.common;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleRse(ResponseStatusException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(ex.getStatusCode());
        pd.setTitle("Request failed");
        pd.setDetail(ex.getReason());
        pd.setProperty("path", req.getRequestURI());
        pd.setProperty("timestamp", Instant.now().toString());
        return ResponseEntity.status(ex.getStatusCode()).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setDetail("One or more fields are invalid");
        pd.setProperty("path", req.getRequestURI());
        pd.setProperty("timestamp", Instant.now().toString());

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));

        pd.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal error");
        pd.setDetail("Unexpected error");
        pd.setProperty("path", req.getRequestURI());
        pd.setProperty("timestamp", Instant.now().toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }
}
