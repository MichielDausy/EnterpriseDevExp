package fact.it.speedrunservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "game") //Replaces Entity for SQL
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Game {
    private int id;
    private String name;
    private String developer;
    private String genre;
    private String platform;
}
