package hello.steamchoice.domain.game.repository;

import hello.steamchoice.domain.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
