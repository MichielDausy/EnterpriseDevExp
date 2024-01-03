package fact.it.profileservice.service;

import fact.it.profileservice.dto.ProfileResponse;
import fact.it.profileservice.model.Profile;
import fact.it.profileservice.repository.ProfileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

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
        return mapToProfileResponse(profile);
    }

    public ProfileResponse getProfileId(String profileName) {
        Profile profile = profileRepository.findByUsernameEquals(profileName);
        return mapToProfileResponse(profile);
    }

}
