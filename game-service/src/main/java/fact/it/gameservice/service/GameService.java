package fact.it.gameservice.service;

import fact.it.gameservice.dto.GameResponse;
import fact.it.gameservice.model.Game;
import fact.it.gameservice.repository.GameRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    private GameResponse mapToGameResponse(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .name(game.getName())
                .description(game.getDescription())
                .build();
    }

    @PostConstruct
    public void loadData() {
        if (gameRepository.count() == 0) {
            Game game0 = new Game();
            game0.setName("The Legend of Zelda: Breath of the Wild");
            game0.setDescription("An action-adventure game set in a vast open world. Players explore the kingdom of Hyrule, solving puzzles, battling enemies, and uncovering the story of the hero Link's quest to defeat the evil Calamity Ganon.");

            Game game1 = new Game();
            game1.setName("The Witcher 3: Wild Hunt");
            game1.setDescription("A sprawling RPG set in a dark fantasy world. Players control Geralt of Rivia, a monster hunter, as he navigates through a morally complex narrative, slaying beasts, making choices that affect the world, and searching for his missing adopted daughter.");

            Game game2 = new Game();
            game2.setName("Portal 2");
            game2.setDescription("A mind-bending puzzle game that challenges players with a series of tests using a portal gun. Players solve physics-based puzzles, manipulate space, and navigate through a compelling narrative that combines humor and intricate level design.");

            // Save games to the repository
            gameRepository.saveAll(Arrays.asList(game0, game1, game2));
        }
    }

    @Transactional(readOnly = true)
    public List<GameResponse> getAllGames() {
        return gameRepository.findByNameNotNull().stream().map(this::mapToGameResponse).toList();
    }

    @Transactional(readOnly = true)
    public GameResponse getGameByName(String name) {
        return mapToGameResponse(gameRepository.findByNameEquals(name));
    }
}
