package fact.it.profileservice.controller;

import fact.it.profileservice.dto.ProfileResponse;
import fact.it.profileservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileResponse> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/getid")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse getProfileId(@RequestParam String profileName) {
        return profileService.getProfileId(profileName);
    }

    @GetMapping("/top5")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse getProfileById(@RequestParam String profileId) {
        return profileService.getProfileById(profileId);
    }
}
