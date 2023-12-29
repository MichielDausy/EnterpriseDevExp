package fact.it.gameservice.repository;

import fact.it.gameservice.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByNameNotNull(); //gets all games

    Game findByNameEquals(String name);
}
