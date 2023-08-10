package com.stmics.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversalResponse {
    private String message;
    private String status;
    private Object data;
    private String error;
    private LocalDateTime timestamp;
}
