package fact.it.speedrunservice.service;

import fact.it.speedrunservice.dto.GameResponse;
import fact.it.speedrunservice.dto.ProfileResponse;
import fact.it.speedrunservice.dto.SpeedrunRequest;
import fact.it.speedrunservice.dto.SpeedrunResponse;
import fact.it.speedrunservice.model.Speedrun;
import fact.it.speedrunservice.repository.SpeedrunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SpeedrunService {
    private final SpeedrunRepository speedrunRepository;
    private final WebClient webClient;
    @Value("${profileservice.baseurl}")
    private String profileServiceBaseUrl;
    @Value("${gameservice.baseurl}")
    private String gameServiceBaseUrl;

    private SpeedrunResponse mapToSpeedrunResponse(Speedrun speedrun) {
        if (speedrun.getEndTime() != null) {
            long durationInSeconds = calculateDuration(speedrun);
            return SpeedrunResponse.builder()
                    .speedrunId(speedrun.getSpeedrunId())
                    .startTime(speedrun.getStartTime())
                    .endTime(speedrun.getEndTime())
                    .duration(formatDuration(durationInSeconds))
                    .build();
        }
        return SpeedrunResponse.builder()
                .startTime(speedrun.getStartTime())
                .speedrunId(speedrun.getSpeedrunId())
                .build();
    }

    //Calculate the difference between the start and end time
    private long calculateDuration(Speedrun speedrun) {
        LocalDateTime startTime = speedrun.getStartTime();
        LocalDateTime endTime = speedrun.getEndTime();
        return Duration.between(startTime, endTime).toSeconds();
    }

    //Format the speedrun duration to hours, minutes and seconds
    private String formatDuration(long durationInSeconds) {
        long hours = durationInSeconds / 3600;
        long minutes = (durationInSeconds % 3600) / 60;
        long seconds = durationInSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public Speedrun startSpeedrun(SpeedrunRequest speedrunRequest) {
        Speedrun speedrun = new Speedrun();
        speedrun.setSpeedrunId(UUID.randomUUID().toString());
        speedrun.setStartTime(LocalDateTime.now());

        //Get the gameId
        GameResponse gameResponse = webClient.get()
                        .uri("http://" + gameServiceBaseUrl + "/api/games/getid",
                                uriBuilder -> uriBuilder.queryParam("gameName", speedrunRequest.getGame()).build())
                                .retrieve()
                                        .bodyToMono(GameResponse.class)
                                                .block();
        //Get the profileId
        ProfileResponse profileResponse = webClient.get()
                .uri("http://" + profileServiceBaseUrl + "/api/profiles/getid",
                        uriBuilder -> uriBuilder.queryParam("profileName", speedrunRequest.getProfile()).build())
                .retrieve()
                .bodyToMono(ProfileResponse.class)
                .block();
        if (gameResponse == null || profileResponse == null) {
            return null;
        }
        speedrun.setGameId(gameResponse.getGameId());
        speedrun.setProfileId(profileResponse.getProfileId());

        speedrunRepository.save(speedrun);
        return speedrun;
    }

    public String endSpeedrun(SpeedrunRequest speedrunRequest) {
        Speedrun speedrun = speedrunRepository.findBySpeedrunIdEquals(speedrunRequest.getSpeedrunId());
        if (speedrun == null) {
            return null;
        }
        speedrun.setEndTime(LocalDateTime.now());
        return formatDuration(calculateDuration(speedrun));
    }

    public List<SpeedrunResponse> getSpeedrunsByProfileName(String profileName) {
        ProfileResponse profileResponse = webClient.get()
                .uri("http://"+ profileServiceBaseUrl + "/api/profiles/getid",
                        uriBuilder -> uriBuilder.queryParam("profileName", profileName).build())
                .retrieve()
                .bodyToMono(ProfileResponse.class)
                .block();
        List<Speedrun> speedruns = speedrunRepository.findByProfileIdEquals(profileResponse.getProfileId());
        return speedruns.stream().map(this::mapToSpeedrunResponse).toList();
    }

    public boolean checkGameId(String gameId) {
        List<Speedrun> speedruns = speedrunRepository.findByGameIdEquals(gameId);
        if (speedruns.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean checkProfileId(String profileId) {
        List<Speedrun> speedruns = speedrunRepository.findByProfileIdEquals(profileId);
        if (speedruns.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<ProfileResponse> getTop5SpeedrunsByGame(String gameId) {
        List<Speedrun> speedruns = speedrunRepository.findByGameIdEquals(gameId);
        List<Speedrun> sortedSpeedruns = speedruns.stream()
                .sorted(Comparator.comparingLong(this::calculateDuration))
                .limit(5) //Pick the first 5
                .toList();

        return sortedSpeedruns.stream()
                .map(speedrun -> {
                    ProfileResponse profile = webClient.get()
                            .uri("http://" + profileServiceBaseUrl + "/api/profiles/top5",
                                    uriBuilder -> uriBuilder.queryParam("profileId", speedrun.getProfileId()).build()
                            ).retrieve().bodyToMono(ProfileResponse.class).block();
                    profile.setSpeedrun(mapToSpeedrunResponse(speedrun));
                    return profile;
                })
                .toList();
    }
}