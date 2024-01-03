package fact.it.gameservice.service;

import fact.it.gameservice.dto.GameResponse;
import fact.it.gameservice.dto.ProfileResponse;
import fact.it.gameservice.model.Game;
import fact.it.gameservice.repository.GameRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final WebClient webClient;
    @Value("${speedrunservice.baseurl}")
    private String speedrunServiceBaseUrl;

    private GameResponse mapToGameResponse(Game game) {
        return GameResponse.builder()
                .gameId(game.getGameId())
                .name(game.getName())
                .build();
    }

    @PostConstruct
    public void loadData() {
        if (gameRepository.count() == 0) {
            for (int i = 0; i < 15; i++) {
                Game game = new Game();
                game.setName("Game_" + i);
                game.setGameId(UUID.randomUUID().toString());
                // set other properties if needed

                gameRepository.save(game);
            }
        }
    }

    public GameResponse getGameId(String gameName) {
        Game game = gameRepository.findByNameEquals(gameName);
        return mapToGameResponse(game);
    }

    public List<ProfileResponse> top5(String gameName) {
        //Check if game exists
        Game game = gameRepository.findByNameEquals(gameName);
        if (game == null) {
            return null;
        }

        // Fetch profiles for the game from the ProfileService
        List<ProfileResponse> profiles = webClient.get()
                .uri("http://" + speedrunServiceBaseUrl + "/api/speedruns/top5",
                        uriBuilder -> uriBuilder.queryParam("gameId", game.getGameId()).build()
                                ).retrieve().bodyToFlux(ProfileResponse.class).collectList().block();

        return profiles;
    }

    public List<GameResponse> getAllGames() {
        List<Game> games = gameRepository.findByIdNotNull();
        return games.stream().map(this::mapToGameResponse).toList();
    }
}