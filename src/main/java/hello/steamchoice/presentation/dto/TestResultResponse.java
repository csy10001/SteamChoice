package hello.steamchoice.presentation.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record TestResultResponse(
        String steamId,
        String type,                 // 최종 성향 (Explorer, Achiever 등)
        int score,                   // 성향 점수
        Map<String, Integer> scores, // 성향별 점수 (차트용)
        LocalDateTime createdAt
) {}

