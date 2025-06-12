package com.company.exception;

import com.company.dto.response.ErrorResponse;
import com.company.constants.ErrorConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle all uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        // You can log the exception here if needed
        return new ResponseEntity<>(ErrorConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle bad request / invalid arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }

    // Handle custom exception like User Not Found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // Add more handlers if needed...
}
