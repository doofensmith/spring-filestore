package com.softlaboratory.storageservice.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiResponse implements Serializable {
    private static final long serialVersionUID = 841731459703183325L;

    private LocalDateTime timestamp;
    private int statusCode;
    private String message;
    private Object data;

}
