package hello.steamchoice.presentation;

import hello.steamchoice.domain.test.service.TestService;
import hello.steamchoice.presentation.dto.TestResultRadarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/{steamId}")
    public ResponseEntity<TestResultRadarResponse> analyze(@PathVariable String steamId) {
        return ResponseEntity.ok(testService.analyze(steamId));
    }
}
