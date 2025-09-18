package hello.steamchoice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient steamWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.steampowered.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient steamStoreWebClient() {
        return WebClient.builder()
                .baseUrl("https://store.steampowered.com")
                .build();
    }
}
