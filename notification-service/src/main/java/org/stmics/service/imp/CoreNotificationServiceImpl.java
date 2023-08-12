package org.stmics.service.imp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.stmics.config.MoveSMSConfiguration;
import org.stmics.dto.NotificationDto;
import org.stmics.repository.TemplateRepository;
import org.stmics.service.CoreNotificationService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreNotificationServiceImpl implements CoreNotificationService {
    private final MoveSMSConfiguration moveSMSConfiguration;
    private final Gson gson;
    private final TemplateRepository templateRepository;

    @Override
    public void sendSms(String phone, String message) {
        if (phone == null || phone.isEmpty()) {
            log.info("Phone number is empty");
            return;
        }
        if (message == null || message.length() < 10) {
            log.info("Message is empty");
            return;
        }
        log.info("Sending Message {} to {}", message, phone);
        URI url =  UriComponentsBuilder.fromUriString(moveSMSConfiguration.getUrl())
                .queryParam("username", moveSMSConfiguration.getUsername())
                .queryParam("api_key", moveSMSConfiguration.getApiKey())
                .queryParam("sender", moveSMSConfiguration.getSender())
                .queryParam("to", "254796407365")
                .queryParam("message", message)
                .queryParam("msgtype", "5")
                .queryParam("dlr", "0")
                .build()
                .toUri();
        WebClient.create()
                .post()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(err -> log.info("Something went wrong {}", err.getMessage()))
                .doOnSuccess(s -> log.info("SMS sent successfully"))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    @Override
    public void sendEmail(String email, String message) {

    }

    @Override
    public Mono<String> parseMessage(NotificationDto notificationDto) {
        log.info("Looking for template called {} of type {}", notificationDto.getTemplateName(), notificationDto.getType());
        return templateRepository.findByNameAndNotificationTypeAndActiveTrue(notificationDto.getTemplateName(), notificationDto.getType())
                .switchIfEmpty(Mono.error(new RuntimeException("Template not found")))
                .flatMap(templates -> {
                    JsonObject messageData = gson.fromJson(gson.toJson(notificationDto.getMessage()), JsonObject.class);
                    String message = templates.getMessage();
                    for (Map.Entry<String, JsonElement> element : messageData.entrySet()) {
                        if (message.contains(String.format("{%s}", element.getKey()))) {
                            message = message.replace(String.format("{%s}", element.getKey()), element.getValue().getAsString());
                        }
                    }
                    return Mono.just(message);
                });
    }
}
