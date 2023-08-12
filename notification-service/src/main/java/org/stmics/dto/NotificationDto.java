package org.stmics.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private String phone;
    private Object message;
    private String email;
    private String type;
    private String templateName;
}