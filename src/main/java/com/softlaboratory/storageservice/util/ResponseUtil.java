package com.softlaboratory.storageservice.util;

import com.softlaboratory.storageservice.domain.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseUtil {

    private ResponseUtil() {
        //
    }

    public static ResponseEntity<Object> build(HttpStatus httpStatus, String message, Object data) {
        return new ResponseEntity<>(ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(httpStatus.value())
                .message(message)
                .data(data)
                .build(), httpStatus);
    }

}
