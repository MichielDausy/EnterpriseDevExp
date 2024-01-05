package fact.it.profileservice.service;

import fact.it.profileservice.dto.ProfileRequest;
import fact.it.profileservice.dto.ProfileResponse;
import fact.it.profileservice.model.Profile;
import fact.it.profileservice.repository.ProfileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final WebClient webClient;
    @Value("${speedrunservice.baseurl}")
    private String speedrunServiceBaseUrl;

    private ProfileResponse mapToProfileResponse(Profile profile) {
        return ProfileResponse.builder()
                .profileId(profile.getProfileId())
                .username(profile.getUsername())
                .email(profile.getEmail())
                .build();
    }

    @PostConstruct
    public void loadData() {
        if (profileRepository.count() == 0) {
            for (int i = 0; i < 15; i++) {
                Profile profile = new Profile();
                profile.setUsername("User_" + i); // Generates usernames User_0, User_1, ..., User_14
                profile.setEmail("User_" + i + "@gmail.com");
                profile.setProfileId(UUID.randomUUID().toString());

                profileRepository.save(profile);
            }
        }
    }

    public List<ProfileResponse> getAllProfiles() {
        List<Profile> profiles = profileRepository.findByIdNotNull();
        return profiles.stream().map(this::mapToProfileResponse).toList();
    }

    public ProfileResponse getProfileById(String profileId) {
        Profile profile = profileRepository.findByProfileIdEquals(profileId);
        if (profile == null) {
            return null;
        }
        return mapToProfileResponse(profile);
    }

    public ProfileResponse getProfileId(String profileName) {
        Profile profile = profileRepository.findByUsernameEquals(profileName);
        return mapToProfileResponse(profile);
    }

    public ProfileResponse createProfile(ProfileRequest profileRequest) {
        Profile profile = new Profile();
        profile.setProfileId(UUID.randomUUID().toString());
        profile.setUsername(profileRequest.getUsername());
        profile.setEmail(profileRequest.getEmail());

        profileRepository.save(profile);
        return mapToProfileResponse(profile);
    }

    public ProfileResponse updateProfile(ProfileRequest profileRequest) {
        Profile profile = profileRepository.findByProfileIdEquals(profileRequest.getProfileId());
        if (profile == null) {
            return null;
        }
        profile.setUsername(profileRequest.getUsername());
        profile.setEmail(profileRequest.getEmail());

        profileRepository.save(profile);
        return mapToProfileResponse(profile);
    }

    public String deleteProfile(ProfileRequest profileRequest) {
        Profile profile = profileRepository.findByProfileIdEquals(profileRequest.getProfileId());
        Boolean profileInUse = webClient.get()
                .uri("http://" + speedrunServiceBaseUrl + "/api/speedruns",
                        uriBuilder -> uriBuilder.queryParam("profileId", profileRequest.getProfileId()).build())
                .retrieve().bodyToMono(Boolean.class).block();
        if (profileInUse) {
            profileRepository.delete(profile);
            return "Profile: " + profile.getUsername() + " deleted";
        }
        return "Failed to delete profile: " + profile.getUsername() + ", profile still linked to speedrun";
    }
}