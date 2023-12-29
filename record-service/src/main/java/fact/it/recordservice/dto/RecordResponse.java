package fact.it.recordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordResponse {
    private Long id;
    private String recordNumber;
    private Duration time;
    private String gameName;
    private String categoryName;
}
