package hello.steamchoice.domain.game.repository;

import hello.steamchoice.domain.game.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    List<UserGame> findBySteamId(String steamId);
}
