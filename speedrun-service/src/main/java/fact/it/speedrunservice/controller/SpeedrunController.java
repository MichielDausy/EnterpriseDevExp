package fact.it.speedrunservice.controller;

import fact.it.speedrunservice.dto.ProfileResponse;
import fact.it.speedrunservice.dto.SpeedrunRequest;
import fact.it.speedrunservice.dto.SpeedrunResponse;
import fact.it.speedrunservice.model.Speedrun;
import fact.it.speedrunservice.service.SpeedrunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speedruns")
@RequiredArgsConstructor
public class SpeedrunController {
    private final SpeedrunService speedrunService;

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.OK)
    public String startSpeedrun(@RequestBody SpeedrunRequest speedrunRequest) {
        Speedrun result = speedrunService.startSpeedrun(speedrunRequest);
        return (result != null ? "Speedrun: " + result.getSpeedrunId() + " created succesfully" : "Speedrun creation failed");
    }

    @PostMapping("/end")
    @ResponseStatus(HttpStatus.OK)
    public String stopSpeedrun(@RequestBody SpeedrunRequest speedrunRequest) {
        String result = speedrunService.endSpeedrun(speedrunRequest);
        return (result != null ? "Speedrun ended with time: " + result: "Speedrun failed to stop");
    }
    @GetMapping("/profiles")
    @ResponseStatus(HttpStatus.OK)
    public List<SpeedrunResponse> getSpeedrunsByProfile(@RequestParam String profileName) {
        return speedrunService.getSpeedrunsByProfileName(profileName);
    }

    @GetMapping("/gameid")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkGameId(@RequestParam String gameId) {
        return speedrunService.checkGameId(gameId);
    }

    @GetMapping("/profileid")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkProfileId(@RequestParam String profileId) {
        return speedrunService.checkProfileId(profileId);
    }

    @GetMapping("/top5")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileResponse> getTop5SpeedrunsByGame(@RequestParam String gameId) {
        return speedrunService.getTop5SpeedrunsByGame(gameId);
    }
}
