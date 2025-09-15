package hello.steamchoice.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SteamApiClient {

    private final WebClient steamWebClient;

    @Value("${steam.api-key}")
    private String apiKey;

    public Map<String, Object> getUserInfo(String steamId) {
        return steamWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamUser/GetPlayerSummaries/v2/")
                        .queryParam("key", apiKey)
                        .queryParam("steamids", steamId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    public Map<String, Object> getOwnedGames(String steamId) {
        return steamWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/IPlayerService/GetOwnedGames/v1/")
                        .queryParam("key", apiKey)
                        .queryParam("steamid", steamId)
                        .queryParam("include_appinfo", true)
                        .queryParam("include_played_free_games", true)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    public Map<String, Object> getPlayerAchievements(String steamId, Long appId) {
        return steamWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamUserStats/GetPlayerAchievements/v1/")
                        .queryParam("key", apiKey)
                        .queryParam("steamid", steamId)
                        .queryParam("appid", appId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
