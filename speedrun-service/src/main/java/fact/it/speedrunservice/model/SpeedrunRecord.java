package fact.it.speedrunservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.DateFormat;

@Document(value = "speedrun") //Replaces Entity for SQL
@AllArgsConstructor
@NoArgsConstructor
@Builder //Enables Lombok’s implemenation of the Builder design pattern!
@Data
public class SpeedrunRecord {
    private int id;
    private int userId;
    private Game game;
    private String category;
    private String time;
    private String date;
}
