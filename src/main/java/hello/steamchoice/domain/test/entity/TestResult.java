package hello.steamchoice.domain.test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String steamId;

    private String type; // 탐험가, 인싸, 킬러 등등
    private int score; // 점수 기반 분석

    private LocalDateTime createdAt;
}
