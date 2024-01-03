package fact.it.speedrunservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String profileId;
    private String username;
    private String email;
    private SpeedrunResponse speedrun;
}
