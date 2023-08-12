package org.stmics.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.stmics.dto.NotificationDto;
import org.stmics.service.CoreNotificationService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoreAsyncOperations {
    private final ObjectMapper objectMapper;
    private final CoreNotificationService coreNotificationService;

    @Bean
    Consumer<String> sendSmsConsumer() {
        return message -> {
            log.info("Sending SMS: {}", message);
            try {
                NotificationDto notificationDto = objectMapper.readValue(message, NotificationDto.class);
                log.info("Sending SMS: {}", notificationDto);
                coreNotificationService.parseMessage(notificationDto)
                        .flatMap(parsedMessage -> {
                            log.info("Sending SMS: {}", message);
                            coreNotificationService.sendSms(notificationDto.getPhone(), parsedMessage);
                            return Mono.just(parsedMessage);
                        })
                        .doOnSuccess(s -> log.info("Message sent: {}", s))
                        .doOnError(err -> log.info("Something went wrong in parsing {}", err.getMessage()))
                        .subscribeOn(Schedulers.boundedElastic())
                        .subscribe();

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        };
    }

}

