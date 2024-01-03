package fact.it.gameservice.controller;

import fact.it.gameservice.dto.GameRequest;
import fact.it.gameservice.dto.GameResponse;
import fact.it.gameservice.dto.ProfileResponse;
import fact.it.gameservice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<GameResponse> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/getid")
    @ResponseStatus(HttpStatus.OK)
    public GameResponse getGameId(@RequestParam String gameName) {
        return gameService.getGameId(gameName);
    }

    @GetMapping("/top5")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileResponse> top5(@RequestParam String game) {
        return gameService.top5(game);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public GameResponse createGame(@RequestBody GameRequest gameRequest) {
        return gameService.createGame(gameRequest);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public GameResponse updateGame(@RequestBody GameRequest gameRequest) {
        return gameService.updateGame(gameRequest);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public String deleteGame(@RequestBody GameRequest gameRequest) {
        return gameService.deleteGame(gameRequest);
    }
}