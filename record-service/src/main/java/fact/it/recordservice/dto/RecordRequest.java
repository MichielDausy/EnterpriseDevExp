package fact.it.recordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordRequest {
    private Duration time;
    private String gameName;
    private String categoryName;
}
