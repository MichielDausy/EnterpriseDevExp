package fact.it.speedrunservice.repository;

import fact.it.speedrunservice.model.Speedrun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeedrunRepository extends JpaRepository<Speedrun, Long> {
    Speedrun findBySpeedrunIdEquals(String speedrunId);

    List<Speedrun> findByProfileIdEquals(String profileId);

    List<Speedrun> findByIdNotNull();

    List<Speedrun> findByGameIdEquals(String gameId);
}
