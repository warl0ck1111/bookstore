package com.example.bookingstore.exceptions;

import com.example.bookingstore.dto.responses.ApiErrorResponse;
import com.example.bookingstore.dto.responses.ApiFailedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InsufficientStockException.class})
    public ResponseEntity<ApiFailedResponse> handleInsufficientStockException(InsufficientStockException ex) {
        log.error("handleInsufficientStockException/: {}", ex.getMessage());
        log.error("handleInsufficientStockException/error: ", ex);

        return new ResponseEntity<>(new ApiFailedResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({AppUserServiceException.class})
    public ResponseEntity<ApiFailedResponse> handleAppUserServiceException(AppUserServiceException ex) {
        log.error("handleAppUserServiceException/: {}", ex.getMessage());
        log.error("handleAppUserServiceException/error: ", ex);

        return new ResponseEntity<>(new ApiFailedResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PaymentServiceException.class})
    public ResponseEntity<ApiFailedResponse> handleCheckoutServiceException(PaymentServiceException ex) {
        log.error("handleCheckoutServiceException/: {}", ex.getMessage());
        log.error("handleCheckoutServiceException/error: ", ex);

        return new ResponseEntity<>(new ApiFailedResponse(ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ApiFailedResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("handleResourceNotFoundException/: {}", ex.getMessage());
        log.error("handleResourceNotFoundException/error: ", ex);

        return new ResponseEntity<>(new ApiFailedResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiFailedResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("handleAccessDeniedException/: {}", ex.getMessage());
        log.error("handleAccessDeniedException/error: ", ex);
        return new ResponseEntity<>(new ApiFailedResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiFailedResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("handleBadCredentialsException/ERROR: ", e);
        return new ResponseEntity<>(new ApiFailedResponse("invalid username or password"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiFailedResponse> handleDisabledException(DisabledException e) {
        log.error("handleDisabledException/ERROR: ", e);
        return new ResponseEntity<>(new ApiFailedResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiFailedResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException/e :", e);
        List<String> errorList = new ArrayList<>();
        e.getAllErrors().forEach(objectError -> errorList.add(objectError.getDefaultMessage()));
        return new ResponseEntity<>(new ApiFailedResponse(errorList.getFirst()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookServiceException.class)
    public ResponseEntity<ApiFailedResponse> handleBookServiceException(BookServiceException e) {
        log.error("handleBookServiceException/e :", e);
        return new ResponseEntity<>(new ApiFailedResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        log.error("handleException/e :", e);
        return new ResponseEntity<>(new ApiErrorResponse("An error occurred while processing your request"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
