package fact.it.speedrunservice.dto;

import fact.it.speedrunservice.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpeedrunRequest {
    private int userId;
    private Game game;
    private String category;
    private String time;
    private String date;
}
