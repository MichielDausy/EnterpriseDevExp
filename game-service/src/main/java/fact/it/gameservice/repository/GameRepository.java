package fact.it.gameservice.repository;

import fact.it.gameservice.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    Game findByNameEquals(String name);
    List<Game> findByIdNotNull();
    Game findByGameIdEquals(String gameId);
}
