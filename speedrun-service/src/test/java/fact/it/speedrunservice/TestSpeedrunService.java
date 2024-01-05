package fact.it.speedrunservice;

import fact.it.speedrunservice.dto.GameResponse;
import fact.it.speedrunservice.dto.ProfileResponse;
import fact.it.speedrunservice.dto.SpeedrunRequest;
import fact.it.speedrunservice.dto.SpeedrunResponse;
import fact.it.speedrunservice.model.Speedrun;
import fact.it.speedrunservice.repository.SpeedrunRepository;
import fact.it.speedrunservice.service.SpeedrunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestSpeedrunService {
    @InjectMocks
    private SpeedrunService speedrunService;

    @Mock
    private SpeedrunRepository speedrunRepository;

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
        ReflectionTestUtils.setField(speedrunService, "gameServiceBaseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(speedrunService, "profileServiceBaseUrl", "http://localhost:8082");
    }

    @Test
    void testStartSpeedrun() {
        // Arrange
        SpeedrunRequest speedrunRequest = new SpeedrunRequest();
        speedrunRequest.setGame("Test Game");
        speedrunRequest.setProfile("Test Profile");

        GameResponse gameResponse = new GameResponse();
        gameResponse.setGameId("123");

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setProfileId("456");

        when(webClient.get())
                .thenReturn(requestHeadersUriSpec)
                .thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(anyString(), any(Function.class)))
                .thenReturn(requestHeadersSpec)
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec)
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(GameResponse.class))
                .thenReturn(Mono.just(gameResponse));

        when(responseSpec.bodyToMono(ProfileResponse.class))
                .thenReturn(Mono.just(profileResponse));

        // Act
        Speedrun speedrun = speedrunService.startSpeedrun(speedrunRequest);

        // Assert
        assertEquals("123", speedrun.getGameId());
        assertEquals("456", speedrun.getProfileId());
    }

    @Test
    void testEndSpeedrun() {
        // Arrange
        LocalDateTime startTime = LocalDateTime.now();

        Speedrun speedrun = new Speedrun();
        speedrun.setSpeedrunId(UUID.randomUUID().toString());
        speedrun.setStartTime(startTime);
        speedrun.setGameId("123");
        speedrun.setProfileId("456");

        SpeedrunRequest speedrunRequest = new SpeedrunRequest();
        speedrunRequest.setSpeedrunId(speedrun.getSpeedrunId());

        when(speedrunRepository.findBySpeedrunIdEquals(speedrun.getSpeedrunId())).thenReturn(speedrun);

        // Act
        String duration = speedrunService.endSpeedrun(speedrunRequest);

        // Assert
        assertNotNull(duration);
        assertTrue(duration.contains(":"));
    }

    @Test
    void testGetSpeedrunsByProfileName() {
        // Arrange
        String profileName = "Test Profile";
        String profileId = "456";

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setProfileId(profileId);

        Speedrun speedrun1 = new Speedrun();
        speedrun1.setProfileId("456");
        speedrun1.setSpeedrunId("123");
        speedrun1.setStartTime(LocalDateTime.now());
        speedrun1.setEndTime(null);

        Speedrun speedrun2 = new Speedrun();
        speedrun2.setProfileId("456");
        speedrun2.setSpeedrunId("124");
        speedrun2.setStartTime(LocalDateTime.now());
        speedrun2.setEndTime(null);

        List<Speedrun> speedruns = Arrays.asList(
                speedrun1,
                speedrun2
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ProfileResponse.class)).thenReturn(Mono.just(profileResponse));

        when(speedrunRepository.findByProfileIdEquals(profileId)).thenReturn(speedruns);

        // Act
        List<SpeedrunResponse> speedrunResponses = speedrunService.getSpeedrunsByProfileName(profileName);

        // Assert
        assertNotNull(speedrunResponses);
        assertEquals(speedruns.size(), speedrunResponses.size());
    }

    @Test
    void testCheckGameIdNotFound() {
        // Arrange
        String gameId = "123";

        when(speedrunRepository.findByGameIdEquals(gameId))
                .thenReturn(new ArrayList<>());

        // Act
        boolean result = speedrunService.checkGameId(gameId);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCheckGameIdFound() {
        // Arrange
        String gameId = "123";

        List<Speedrun> speedruns = new ArrayList<>();

        Speedrun speedrun = new Speedrun();
        speedrun.setStartTime(LocalDateTime.now());
        speedrun.setGameId("123");
        speedrun.setSpeedrunId(UUID.randomUUID().toString());
        speedrun.setProfileId("456");

        speedruns.add(speedrun);

        when(speedrunRepository.findByGameIdEquals(gameId)).thenReturn(speedruns);

        // Act
        boolean result = speedrunService.checkGameId(gameId);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckProfileIdNotFound() {
        // Arrange
        String profileId = "456";

        when(speedrunRepository.findByProfileIdEquals(profileId))
                .thenReturn(new ArrayList<>());

        // Act
        boolean result = speedrunService.checkProfileId(profileId);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCheckProfileIdFound() {
        // Arrange
        String profileId = "456";

        List<Speedrun> speedruns = new ArrayList<>();

        Speedrun speedrun = new Speedrun();
        speedrun.setStartTime(LocalDateTime.now());
        speedrun.setGameId("123");
        speedrun.setSpeedrunId(UUID.randomUUID().toString());
        speedrun.setProfileId("456");

        speedruns.add(speedrun);

        when(speedrunRepository.findByProfileIdEquals(profileId)).thenReturn(speedruns);

        // Act
        boolean result = speedrunService.checkProfileId(profileId);

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetTop5SpeedrunsByGame() {
        // Arrange
        String gameId = "123";

        List<Speedrun> speedruns = new ArrayList<>();
        List<ProfileResponse> profileResponses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Speedrun speedrun = new Speedrun();
            speedrun.setSpeedrunId(UUID.randomUUID().toString());
            speedrun.setStartTime(LocalDateTime.now());
            speedrun.setEndTime(LocalDateTime.now().plusHours(1));
            speedrun.setGameId("12" + i);
            speedrun.setProfileId("45" + i);

            speedruns.add(speedrun);

            SpeedrunResponse speedrunResponse = new SpeedrunResponse();
            speedrunResponse.setSpeedrunId(speedrun.getSpeedrunId());
            speedrunResponse.setStartTime(speedrun.getStartTime());
            speedrunResponse.setEndTime(speedrun.getEndTime());
            speedrunResponse.setDuration("01:00:00");

            ProfileResponse profileResponse = new ProfileResponse();
            profileResponse.setUsername("User" + i);
            profileResponse.setEmail("user" + i + "@gmail.com");
            profileResponse.setProfileId("45" + i);
            profileResponse.setSpeedrun(speedrunResponse);

            profileResponses.add(profileResponse);
        }

        when(speedrunRepository.findByGameIdEquals(gameId)).thenReturn(speedruns);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ProfileResponse.class)).thenReturn(Mono.just(new ProfileResponse()));

        // Act
        List<ProfileResponse> top5Speedruns = speedrunService.getTop5SpeedrunsByGame(gameId);

        // Assert
        assertNotNull(top5Speedruns);
        assertEquals(5, top5Speedruns.size());
    }
}
