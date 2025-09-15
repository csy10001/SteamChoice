package hello.steamchoice.domain.user.repository;

import hello.steamchoice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
