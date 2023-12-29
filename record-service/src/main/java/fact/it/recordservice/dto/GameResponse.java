package fact.it.recordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameResponse {
    private Long id;
    private String name;
    private String description;
    private String categoryName; //link with category service
}
