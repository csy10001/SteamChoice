package hello.steamchoice.domain.game.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserGame {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String steamId;   // User.steamId 참조
    private Long appId;       // Game.appId 참조

    private int playtime;     // 분 단위
    private float achievementRate;
}
