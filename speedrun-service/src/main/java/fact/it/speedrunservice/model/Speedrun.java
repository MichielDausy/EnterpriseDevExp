package fact.it.speedrunservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "speedrun")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Speedrun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String speedrunId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String profileId;
    private String gameId;
}
