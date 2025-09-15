package hello.steamchoice.domain.test.service;

import hello.steamchoice.domain.game.entity.UserGame;
import hello.steamchoice.domain.game.repository.UserGameRepository;
import hello.steamchoice.domain.test.entity.TestResult;
import hello.steamchoice.domain.test.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserGameRepository userGameRepository;
    private final TestResultRepository testResultRepository;

    @Transactional
    public TestResult analyze(String steamId) {
        List<UserGame> games = userGameRepository.findBySteamId(steamId);

        if (games.isEmpty()) {
            throw new RuntimeException("유저의 게임이 없습니다." + steamId);
        }

        int totalPlaytime = games.stream().mapToInt(UserGame::getPlaytime).sum();
        int gameCount = games.size();

        String type;
        int score = 0;

        if (gameCount > 300) {
            type = "Collector";
            score = gameCount;
        } else if (games.stream().anyMatch(g -> g.getPlaytime() > 5000)) {
            type = "Achiever";
            score = 90;
        } else if (totalPlaytime > 10000) {
            type = "Explorer";
            score = 80;
        } else {
            type = "Socializer";
            score = 60;
        }

        TestResult result = TestResult.builder()
                .steamId(steamId)
                .type(type)
                .score(score)
                .createdAt(LocalDateTime.now())
                .build();

        return testResultRepository.save(result);
    }
}