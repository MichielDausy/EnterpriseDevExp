package fact.it.profileservice;

import fact.it.profileservice.dto.ProfileRequest;
import fact.it.profileservice.dto.ProfileResponse;
import fact.it.profileservice.model.Profile;
import fact.it.profileservice.repository.ProfileRepository;
import fact.it.profileservice.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestProfileService {
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

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
        ReflectionTestUtils.setField(profileService, "speedrunServiceBaseUrl", "http://localhost:8081");
    }

    @Test
    void testGetProfileById_Success() {
        //Arrange
        Profile profile = new Profile();
        profile.setProfileId("1");
        profile.setUsername("User");
        profile.setEmail("user@gmail.com");

        when(profileRepository.findByProfileIdEquals("1")).thenReturn(profile);

        //Act
        ProfileResponse profileResponse = profileService.getProfileById("1");

        //Assert
        assertEquals("1", profileResponse.getProfileId());
        assertEquals("User", profileResponse.getUsername());
        assertEquals("user@gmail.com", profileResponse.getEmail());
    }

    @Test
    void testGetProfileById_Failure() {
        //Arrange
        //Act
        ProfileResponse profileResponse = profileService.getProfileById("1");

        //Assert
        assertNull(profileResponse);
    }

    @Test
    void testCreateProfile_Success() {
        //Arrange
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setUsername("User");
        profileRequest.setEmail("user@gmail.com");

        //Act
        ProfileResponse profileResponse = profileService.createProfile(profileRequest);

        //Assert
        assertEquals("User", profileResponse.getUsername());
        assertEquals("user@gmail.com", profileResponse.getEmail());

    }

    @Test
    void testUpdateProfile_Success() {
        //Arrange
        Profile profile = new Profile();
        profile.setProfileId("1");
        profile.setUsername("User");
        profile.setEmail("user@gmail.com");

        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setProfileId("1");
        profileRequest.setUsername("UpdatedUser");
        profileRequest.setEmail("updateduser@gmail.com");

        when(profileRepository.findByProfileIdEquals("1")).thenReturn(profile);

        //Act
        ProfileResponse profileResponse = profileService.updateProfile(profileRequest);

        //Assert
        assertEquals("1", profileResponse.getProfileId());
        assertEquals("UpdatedUser", profileResponse.getUsername());
        assertEquals("updateduser@gmail.com", profileResponse.getEmail());
    }

    @Test
    void testUpdateProfile_Failure() {
        //Arrange
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setProfileId("1");
        profileRequest.setUsername("UpdatedUser");
        profileRequest.setEmail("updateduser@gmail.com");

        //Act
        ProfileResponse profileResponse = profileService.updateProfile(profileRequest);

        //Assert
        assertNull(profileResponse);
    }

    @Test
    void testDeleteProfile_Success() {
        //Arrange
        Profile profile = new Profile();
        profile.setProfileId("1");
        profile.setUsername("User");
        profile.setEmail("user@gmail.com");

        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setProfileId(profile.getProfileId());

        // Save the profile in the repository
        when(profileRepository.findByProfileIdEquals(profile.getProfileId())).thenReturn(profile);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.TRUE));

        //Act
        String result = profileService.deleteProfile(profileRequest);

        //Assert
        assertEquals("Profile: User deleted", result);

    }

    @Test
    void testDeleteProfile_Failure() {
        //Arrange
        Profile profile = new Profile();
        profile.setProfileId("1");
        profile.setUsername("User");
        profile.setEmail("user@gmail.com");

        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setProfileId(profile.getProfileId());

        // Save the profile in the repository
        when(profileRepository.findByProfileIdEquals(profile.getProfileId())).thenReturn(profile);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.FALSE));

        //Act
        String result = profileService.deleteProfile(profileRequest);

        //Assert
        assertEquals("Failed to delete profile: User, profile still linked to speedrun", result);

    }
}
