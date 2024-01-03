package fact.it.profileservice.controller;

import fact.it.profileservice.dto.ProfileRequest;
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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse createProfile(@RequestBody ProfileRequest profileRequest) {
        return profileService.createProfile(profileRequest);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse updateProfile(@RequestBody ProfileRequest profileRequest) {
        return profileService.updateProfile(profileRequest);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public String deleteProfile(@RequestBody ProfileRequest profileRequest) {
        return profileService.deleteProfile(profileRequest);
    }
}
