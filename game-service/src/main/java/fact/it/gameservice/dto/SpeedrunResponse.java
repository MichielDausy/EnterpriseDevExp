package fact.it.gameservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeedrunResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String duration;
}
