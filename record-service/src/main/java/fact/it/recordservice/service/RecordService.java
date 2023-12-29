package fact.it.recordservice.service;

import fact.it.recordservice.dto.CategoryResponse;
import fact.it.recordservice.dto.GameResponse;
import fact.it.recordservice.dto.RecordRequest;
import fact.it.recordservice.dto.RecordResponse;
import fact.it.recordservice.model.Record;
import fact.it.recordservice.repository.RecordRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {
    private final RecordRepository recordRepository;
    private final WebClient webClient;

    private RecordResponse mapToRecordResponse(Record record) {
        return RecordResponse.builder()
                .id(record.getId())
                .recordNumber(record.getRecordNumber())
                .time(record.getTime())
                .gameName(record.getGameName())
                .categoryName(record.getCategoryName())
                .build();
    }

    @PostConstruct
    private void loadData() {
        if (recordRepository.count() == 0) {
            Record record0 = new Record();
            record0.setRecordNumber(UUID.randomUUID().toString());
            record0.setGameName("The Legend of Zelda: Breath of the Wild");
            record0.setCategoryName("any%");
            record0.setTime(Duration.ofMinutes(15).plusSeconds(32));

            Record record1 = new Record();
            record1.setRecordNumber(UUID.randomUUID().toString());
            record1.setGameName("The Witcher 3: Wild Hunt");
            record1.setCategoryName("100%");
            record1.setTime(Duration.ofHours(3).plusMinutes(12).plusSeconds(20));

            // Save records
            recordRepository.saveAll(Arrays.asList(record0, record1));
        }

    }

    public boolean createOrUpdateRecord(String recordNumber, RecordRequest recordRequest) {
        Record record;
        if (recordNumber == null) {
            record = new Record();
            record.setRecordNumber(UUID.randomUUID().toString());
        } else {
            record = recordRepository.findByRecordNumberEquals(recordNumber);
        }
        record.setTime(recordRequest.getTime());

        //Checks if the category name exists and gets the category name from the category-service
        CategoryResponse categoryResponse = webClient.get()
                .uri("http://localhost:8080/api/category",
                        uriBuilder -> uriBuilder.queryParam("name", recordRequest.getCategoryName()).build())
                .retrieve()
                .bodyToMono(CategoryResponse.class)
                .block();

        GameResponse gameResponse = webClient.get()
                .uri("http://localhost:8082/api/games",
                        uriBuilder -> uriBuilder.queryParam("name", recordRequest.getGameName()).build())
                .retrieve()
                .bodyToMono(GameResponse.class)
                .block();

        if (categoryResponse == null) {
            return false;
        } else {
            record.setCategoryName(categoryResponse.getName());
        }
        if (gameResponse == null) {
            return false;
        } else {
            record.setGameName(gameResponse.getName());
        }
        recordRepository.save(record);
        return true;
    }

    public List<RecordResponse> getAllRecordsOfGame(String gameName) {
        List<Record> records = recordRepository.findByGameNameEquals(gameName);
        return records.stream().map(this::mapToRecordResponse).toList();
    }

    public List<RecordResponse> getAllRecords() {
        List<Record> records = recordRepository.findByIdNotNull();
        return records.stream().map(this::mapToRecordResponse).toList();
    }

    public boolean deleteRecord(String recordNumber) {
        Record record = recordRepository.findByRecordNumberEquals(recordNumber);
        if (record == null) {
            return false;
        }
        recordRepository.delete(record);
        return true;
    }
}