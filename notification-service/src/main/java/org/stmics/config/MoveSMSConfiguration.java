package org.stmics.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "movesms")
@Getter
@Setter
public class MoveSMSConfiguration {
    private String username;
    private String apiKey;
    private String sender;
    private String url;
}
