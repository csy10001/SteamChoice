package hello.steamchoice.domain.test.service;

import hello.steamchoice.domain.game.entity.UserGame;
import hello.steamchoice.domain.game.repository.UserGameRepository;
import hello.steamchoice.domain.test.entity.TestResult;
import hello.steamchoice.domain.test.repository.TestResultRepository;
import hello.steamchoice.presentation.dto.TestResultRadarResponse;
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
    public TestResultRadarResponse analyze(String steamId) {
        List<UserGame> games = userGameRepository.findBySteamId(steamId);

        if (games.isEmpty()) {
            throw new RuntimeException("No games found for steamId=" + steamId);
        }

        int gameCount = games.size();
        int totalPlaytime = games.stream().mapToInt(UserGame::getPlaytime).sum();

        // ---------------------------
        // 성향별 점수판 초기화
        // ---------------------------
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Collector", 0);
        scores.put("Achiever", 0);
        scores.put("Explorer", 0);
        scores.put("Socializer", 0);
        scores.put("Killer", 0);

        // ---------------------------
        // Collector (게임 수 기준)
        // ---------------------------
        if (gameCount > 200) scores.computeIfPresent("Collector", (k, v) -> v + 50);
        if (gameCount > 500) scores.computeIfPresent("Collector", (k, v) -> v + 20);

        // ---------------------------
        // Achiever (플레이타임 & 업적률)
        // ---------------------------
        if (games.stream().anyMatch(g -> g.getPlaytime() > 1000)) {
            scores.computeIfPresent("Achiever", (k, v) -> v + 40);
        }
        if (games.stream().mapToDouble(UserGame::getAchievementRate).average().orElse(0) > 50.0) {
            scores.computeIfPresent("Achiever", (k, v) -> v + 30);
        }

        // ---------------------------
        // Explorer (총 플레이타임 기준)
        // ---------------------------
        if (totalPlaytime > 20000) {
            scores.computeIfPresent("Explorer", (k, v) -> v + 40);
        }

        // ---------------------------
        // Socializer (임시 로직: 업적률 > 0 게임 비중)
        // ---------------------------
        long multiCount = games.stream().filter(g -> g.getAchievementRate() > 0).count();
        if ((double) multiCount / gameCount > 0.5) {
            scores.computeIfPresent("Socializer", (k, v) -> v + 40);
        }

        // ---------------------------
        // Killer (TODO: 장르 데이터 추가 시 개선)
        // ---------------------------

        // ---------------------------
        // 최종 성향 선택
        // ---------------------------
        String bestType = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Explorer");

        int bestScore = scores.get(bestType);

        // ---------------------------
        // DB 저장
        // ---------------------------
        TestResult result = TestResult.builder()
                .steamId(steamId)
                .type(bestType)
                .score(bestScore)
                .createdAt(LocalDateTime.now())
                .build();
        testResultRepository.save(result);

        // ---------------------------
        // Radar Chart용 데이터 변환
        // ---------------------------
        List<String> labels = List.of("Collector", "Achiever", "Explorer", "Socializer", "Killer");
        List<Integer> data = labels.stream().map(scores::get).toList();

        return new TestResultRadarResponse(
                steamId,
                bestType,
                bestScore,
                labels,
                data,
                result.getCreatedAt()
        );
    }
}
