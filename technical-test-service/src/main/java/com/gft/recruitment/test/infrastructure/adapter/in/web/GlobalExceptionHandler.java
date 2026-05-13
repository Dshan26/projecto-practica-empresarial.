package com.gft.recruitment.test.infrastructure.adapter.in.web;

import com.gft.recruitment.test.domain.exception.IncompleteAnswersException;
import com.gft.recruitment.test.domain.exception.TestAlreadySubmittedException;
import com.gft.recruitment.test.domain.exception.TestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TestNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTestNotFound(TestNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TestAlreadySubmittedException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadySubmitted(TestAlreadySubmittedException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(IncompleteAnswersException.class)
    public ResponseEntity<Map<String, Object>> handleIncompleteAnswers(IncompleteAnswersException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
        return ResponseEntity.status(status).body(body);
    }
}
