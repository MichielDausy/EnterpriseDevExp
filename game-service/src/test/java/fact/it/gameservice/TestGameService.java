package fact.it.gameservice;

import fact.it.gameservice.dto.GameRequest;
import fact.it.gameservice.dto.GameResponse;
import fact.it.gameservice.dto.ProfileResponse;
import fact.it.gameservice.dto.SpeedrunResponse;
import fact.it.gameservice.model.Game;
import fact.it.gameservice.repository.GameRepository;
import fact.it.gameservice.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestGameService {
    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(gameService, "speedrunServiceBaseUrl", "http://localhost:8081");
    }

    @Test
    void testGetGameId_Success() {
        // Arrange
        Game game = new Game();
        game.setGameId("123");
        game.setName("Test Game");

        when(gameRepository.findByNameEquals("Test Game")).thenReturn(game);

        // Act
        GameResponse gameResponse = gameService.getGameId("Test Game");

        // Assert
        assertEquals("123", gameResponse.getGameId());
        assertEquals("Test Game", gameResponse.getName());
    }

    @Test
    void testGetGameId_Failure() {
        // Arrange
        // Act
        GameResponse gameResponse = gameService.getGameId("Test Game");

        // Assert
        assertNull(gameResponse);
    }

    @Test
    void testTop5_Success() {
        // Arrange
        Game game = new Game();
        game.setGameId("123");
        game.setName("Test Game");

        List<ProfileResponse> mockProfiles = new ArrayList<>();
        List<LocalDateTime> startTimes = new ArrayList<>();
        List<LocalDateTime> endTimes = new ArrayList<>();
        List<String> durations = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            startTimes.add(LocalDateTime.now());
            endTimes.add(startTimes.get(i).plusMinutes(i));
            durations.add("00:00:0" + i);

            SpeedrunResponse speedrunResponse = new SpeedrunResponse();
            speedrunResponse.setStartTime(startTimes.get(i));
            speedrunResponse.setEndTime(endTimes.get(i));
            speedrunResponse.setDuration(durations.get(i));

            ProfileResponse profileResponse = new ProfileResponse();
            profileResponse.setUsername("User_" + i);
            profileResponse.setEmail("user_" + i + "@gmail.com");
            profileResponse.setSpeedrun(speedrunResponse);

            mockProfiles.add(profileResponse);
        }

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ProfileResponse.class)).thenReturn(Flux.fromIterable(mockProfiles));

        when(gameRepository.findByNameEquals("Test Game")).thenReturn(game);
        // Act
        List<ProfileResponse> profiles = gameService.top5("Test Game");

        // Assert
        for (int i = 0; i < 10; i++) {
            assertEquals("User_" + i, profiles.get(i).getUsername());
            assertEquals("user_" + i + "@gmail.com", profiles.get(i).getEmail());
            assertEquals(startTimes.get(i), profiles.get(i).getSpeedrun().getStartTime());
            assertEquals(endTimes.get(i), profiles.get(i).getSpeedrun().getEndTime());
            assertEquals(durations.get(i), profiles.get(i).getSpeedrun().getDuration());
        }
    }

    @Test
    void testTop5_GameNotFound_Failure() {
        // Arrange
        // Act
        List<ProfileResponse> profiles = gameService.top5("Test Game");

        // Assert
        assertNull(profiles);
    }

    @Test
    void testTop5_SpeedrunsNotFound_Failure() {
        // Arrange
        Game game = new Game();
        game.setGameId("123");
        game.setName("Test Game");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ProfileResponse.class)).thenReturn(null);

        when(gameRepository.findByNameEquals("Test Game")).thenReturn(game);
        // Act
        List<ProfileResponse> profiles = gameService.top5("Test Game");

        // Assert
        assertNull(profiles);
    }

    @Test
    void testGetAllGames() {
        // Arrange
        Game mockGame = new Game();
        mockGame.setGameId("123");
        mockGame.setName("Test Game");

        when(gameRepository.findByIdNotNull()).thenReturn(Collections.singletonList(mockGame));

        // Act
        List<GameResponse> games = gameService.getAllGames();

        // Assert
        assertEquals(1, games.size());
        assertEquals("123", games.get(0).getGameId());
        assertEquals("Test Game", games.get(0).getName());
    }

    @Test
    void testCreateGame_Success() {
        // Arrange
        GameRequest gameRequest = new GameRequest();
        gameRequest.setName("New Game");

        // Act
        GameResponse gameResponse = gameService.createGame(gameRequest);

        // Assert
        assertEquals("New Game", gameResponse.getName());
    }

    @Test
    void testUpdateGame_Success() {
        // Arrange
        Game mockGame = new Game();
        mockGame.setGameId("123");
        mockGame.setName("Old Game");

        GameRequest gameRequest = new GameRequest();
        gameRequest.setGameId("123");
        gameRequest.setName("Updated Game");

        when(gameRepository.findByGameIdEquals("123")).thenReturn(mockGame);

        // Act
        GameResponse gameResponse = gameService.updateGame(gameRequest);

        // Assert
        assertEquals("123", gameResponse.getGameId());
        assertEquals("Updated Game", gameResponse.getName());
    }

    @Test
    void testUpdateGame_Failure() {
        // Arrange
        GameRequest gameRequest = new GameRequest();
        gameRequest.setGameId("124");
        gameRequest.setName("Updated Game");

        // Act
        GameResponse gameResponse = gameService.updateGame(gameRequest);

        // Assert
        assertNull(gameResponse);
    }

    @Test
    void testDeleteGame_Success() {
        // Arrange
        Game mockGame = new Game();
        mockGame.setGameId("123");
        mockGame.setName("Test Game");

        GameRequest gameRequest = new GameRequest();
        gameRequest.setGameId("123");

        when(gameRepository.findByGameIdEquals("123")).thenReturn(mockGame);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.TRUE));

        // Act
        String result = gameService.deleteGame(gameRequest);

        // Assert
        assertEquals("Game: Test Game deleted", result);
    }

    @Test
    void testDeleteGame_Failure() {
        // Arrange
        Game mockGame = new Game();
        mockGame.setGameId("123");
        mockGame.setName("Test Game");

        GameRequest gameRequest = new GameRequest();
        gameRequest.setGameId("123");

        when(gameRepository.findByGameIdEquals("123")).thenReturn(mockGame);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.FALSE));

        // Act
        String result = gameService.deleteGame(gameRequest);

        // Assert
        assertEquals("Failed to delete game: Test Game, game still in use", result);
    }
}
