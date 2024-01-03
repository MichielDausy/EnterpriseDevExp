package fact.it.speedrunservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpeedrunResponse {
    private String speedrunId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String duration;
}
