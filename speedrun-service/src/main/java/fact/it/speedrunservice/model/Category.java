package fact.it.speedrunservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "category") //Replaces Entity for SQL
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Category {
    private int id;
    private String name;
    private String description;
}
