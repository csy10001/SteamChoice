package hello.steamchoice.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SteamStoreApiClient {

    private final WebClient steamStoreWebClient;

    /**
     * 특정 게임 상세 정보 조회
     */
    public Map<String, Object> getGameDetails(Long appId) {
        return steamStoreWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/appdetails")
                        .queryParam("appids", appId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    /**
     * 인기 게임 카테고리 가져오기 (신작, 세일, 인기작 등)
     */
    public Map<String, Object> getFeaturedCategories() {
        return steamStoreWebClient.get()
                .uri("/api/featuredcategories")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}

