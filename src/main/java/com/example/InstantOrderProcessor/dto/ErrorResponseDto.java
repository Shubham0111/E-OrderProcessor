package com.example.InstantOrderProcessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ErrorResponseDto {
    private Integer status;
    private String message;
    private String error;
}
