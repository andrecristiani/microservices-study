package com.study.store.exception.handler;

import com.study.store.exception.InsufficientStockException;
import com.study.store.exception.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeignExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<?> handleExceptionOfItemNotFound(ItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = InsufficientStockException.class)
    public ResponseEntity<?> handleExceptionOfInsufficientStock(InsufficientStockException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
