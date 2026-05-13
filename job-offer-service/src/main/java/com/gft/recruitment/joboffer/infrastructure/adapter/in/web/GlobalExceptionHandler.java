package com.gft.recruitment.joboffer.infrastructure.adapter.in.web;

import com.gft.recruitment.joboffer.domain.exception.InvalidJobOfferException;
import com.gft.recruitment.joboffer.domain.exception.JobOfferNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobOfferNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(JobOfferNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", "NOT_FOUND",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    @ExceptionHandler(InvalidJobOfferException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJobOffer(InvalidJobOfferException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "BAD_REQUEST",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
