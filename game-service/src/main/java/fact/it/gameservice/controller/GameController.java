package fact.it.gameservice.controller;

import fact.it.gameservice.dto.GameResponse;
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
    public List<GameResponse> getAllGames(){
        return gameService.getAllGames();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GameResponse getGameByName(@RequestParam String name) {
        return gameService.getGameByName(name);
    }
}
