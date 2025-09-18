package hello.steamchoice.domain.test.service;

import hello.steamchoice.domain.game.entity.UserGame;
import hello.steamchoice.domain.game.repository.UserGameRepository;
import hello.steamchoice.domain.test.entity.TestResult;
import hello.steamchoice.domain.test.repository.TestResultRepository;
import hello.steamchoice.presentation.dto.TestResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserGameRepository userGameRepository;
    private final TestResultRepository testResultRepository;

    @Transactional
    public TestResultResponse analyze(String steamId) {
        List<UserGame> games = userGameRepository.findBySteamId(steamId);

        if (games.isEmpty()) {
            throw new RuntimeException("No games found for steamId=" + steamId);
        }

        int gameCount = games.size();
        int totalPlaytime = games.stream().mapToInt(UserGame::getPlaytime).sum();

        // 성향별 점수판
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Collector", 0);
        scores.put("Achiever", 0);
        scores.put("Explorer", 0);
        scores.put("Socializer", 0);
        scores.put("Killer", 0);

        // Collector
        if (gameCount > 200) scores.computeIfPresent("Collector", (k, v) -> v + 50);
        if (gameCount > 500) scores.computeIfPresent("Collector", (k, v) -> v + 20);

        // Achiever
        if (games.stream().anyMatch(g -> g.getPlaytime() > 1000)) {
            scores.computeIfPresent("Achiever", (k, v) -> v + 40);
        }
        if (games.stream().mapToDouble(UserGame::getAchievementRate).average().orElse(0) > 50.0) {
            scores.computeIfPresent("Achiever", (k, v) -> v + 30);
        }

        // Explorer
        if (totalPlaytime > 20000) {
            scores.computeIfPresent("Explorer", (k, v) -> v + 40);
        }

        // Socializer (임시 로직)
        long multiCount = games.stream().filter(g -> g.getAchievementRate() > 0).count();
        if ((double) multiCount / gameCount > 0.5) {
            scores.computeIfPresent("Socializer", (k, v) -> v + 40);
        }

        // Killer (장르 데이터 추가되면 개선 가능)
        // 예: FPS, PvP 장르 비율이 높은 경우 점수 추가

        // 최종 성향
        String bestType = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Explorer");

        int bestScore = scores.get(bestType);

        // DB 저장 (TestResult 엔티티)
        TestResult result = TestResult.builder()
                .steamId(steamId)
                .type(bestType)
                .score(bestScore)
                .createdAt(LocalDateTime.now())
                .build();
        testResultRepository.save(result);

        // DTO 변환
        return new TestResultResponse(
                steamId,
                bestType,
                bestScore,
                scores,
                result.getCreatedAt()
        );
    }
}
