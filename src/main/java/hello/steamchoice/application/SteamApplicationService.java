package hello.steamchoice.application;

import hello.steamchoice.domain.game.entity.Game;
import hello.steamchoice.domain.game.entity.UserGame;
import hello.steamchoice.domain.game.repository.GameRepository;
import hello.steamchoice.domain.game.repository.UserGameRepository;
import hello.steamchoice.domain.user.entity.User;
import hello.steamchoice.domain.user.repository.UserRepository;
import hello.steamchoice.infrastructure.SteamApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SteamApplicationService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserGameRepository userGameRepository;
    private final SteamApiClient steamApiClient;

    // 유저 정보 조회 후 DB 저장
    @Transactional
    public User fetchAndSaveUser(String steamId) {
        Map<String, Object> response = steamApiClient.getUserInfo(steamId);

        Map<?, ?> playersResponse = (Map<?, ?>) response.get("response");
        List<?> players = (List<?>) playersResponse.get("players");

        if (players.isEmpty()) {
            throw new RuntimeException("Steam user not found: " + steamId);
        }

        Map<?, ?> player = (Map<?, ?>) players.get(0);

        User user = new User(
                (String) player.get("steamid"),
                (String) player.get("personaname"),
                (String) player.get("avatarfull")
        );

        return userRepository.save(user);
    }

    // 보유 게임 목록 저장
    @Transactional
    public void fetchAndSaveGames(String steamId) {
        Map<String, Object> response = steamApiClient.getOwnedGames(steamId);

        Map<?, ?> responseBody = (Map<?, ?>) response.get("response");
        List<?> games = (List<?>) responseBody.get("games");

        if (games == null) return;

        for (Object obj : games) {
            Map<?, ?> gameMap = (Map<?, ?>) obj;

            Long appId = ((Number) gameMap.get("appid")).longValue();
            String name = (String) gameMap.get("name");
            Integer playtime = (Integer) gameMap.get("playtime_forever");

            // 게임 정보 저장
            Game game = new Game(appId, name, null); // 장르는 추후 Store API로 보완 가능
            gameRepository.save(game);

            // 유저 게임 기록 저장
            UserGame userGame = new UserGame(
                    null,
                    steamId,
                    appId,
                    playtime != null ? playtime : 0,
                    0.0f // 업적률은 나중에 채울 생각
            );
            userGameRepository.save(userGame);
        }
    }
}
