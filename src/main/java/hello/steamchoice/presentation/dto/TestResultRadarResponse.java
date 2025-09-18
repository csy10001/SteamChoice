package hello.steamchoice.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TestResultRadarResponse(
        String steamId,
        String type,
        int score,
        List<String> labels,   // ["Collector", "Achiever", ...]
        List<Integer> data,    // [70, 40, 20, 10, 0]
        List<String> recommendedGames, // 추천 게임 리스트
        LocalDateTime createdAt
) {}
