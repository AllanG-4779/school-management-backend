package org.stmics.service;

import org.stmics.dto.NotificationDto;
import reactor.core.publisher.Mono;

public interface CoreNotificationService {
    void sendSms(String phone, String message);
    void sendEmail(String email, String message);
    Mono<String> parseMessage(NotificationDto notificationDto);
}
