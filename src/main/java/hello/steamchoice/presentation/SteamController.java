package hello.steamchoice.presentation;

import hello.steamchoice.application.SteamApplicationService;
import hello.steamchoice.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/steam")
@RequiredArgsConstructor
public class SteamController {

    private final SteamApplicationService steamApplicationService;

    @PostMapping("/user/{steamId}")
    public ResponseEntity<User> fetchAndSaveUser(@PathVariable String steamId) {
        return ResponseEntity.ok(steamApplicationService.fetchAndSaveUser(steamId));
    }

    @PostMapping("/games/{steamId}")
    public ResponseEntity<String> fetchAndSaveGames(@PathVariable String steamId) {
        steamApplicationService.fetchAndSaveGames(steamId);
        return ResponseEntity.ok("게임이 성공적으로 저장되었습니다.");
    }
}
