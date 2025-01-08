package com.spring_boot.ecommerce_new.exceptions;

import com.spring_boot.ecommerce_new.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyGlobalException {
    @ExceptionHandler (ApiException.class)
    public ResponseEntity<ApiResponse> myAPIException(ApiException e){
        String message=e.getMessage();


        ApiResponse a= new ApiResponse(message,false);

        return new ResponseEntity<ApiResponse>(a, HttpStatus.BAD_REQUEST);

    }

}
