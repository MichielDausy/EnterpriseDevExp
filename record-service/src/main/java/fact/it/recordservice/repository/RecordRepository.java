package fact.it.recordservice.repository;

import fact.it.recordservice.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findByGameNameEquals(String gameName);

    List<Record> findByIdNotNull();

    Record findByRecordNumberEquals(String recordNumber);
}
