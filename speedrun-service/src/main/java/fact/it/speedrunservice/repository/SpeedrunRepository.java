package fact.it.speedrunservice.repository;

import fact.it.speedrunservice.model.SpeedrunRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpeedrunRepository extends MongoRepository<SpeedrunRecord, String> {
    List<SpeedrunRecord> findByUserIdIn(List<Integer> userId);
}
