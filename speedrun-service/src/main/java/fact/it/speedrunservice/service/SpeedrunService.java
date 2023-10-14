package fact.it.speedrunservice.service;

import fact.it.speedrunservice.dto.SpeedrunRequest;
import fact.it.speedrunservice.dto.SpeedrunResponse;
import fact.it.speedrunservice.model.SpeedrunRecord;
import fact.it.speedrunservice.repository.SpeedrunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeedrunService {
    private final SpeedrunRepository speedrunRepository;

    private SpeedrunResponse mapToSpeedrunResponse(SpeedrunRecord speedrunRecord){
        return SpeedrunResponse.builder()
                .id(speedrunRecord.getId())
                .userId(speedrunRecord.getUserId())
                .game(speedrunRecord.getGame())
                .category(speedrunRecord.getCategory())
                .time(speedrunRecord.getTime())
                .date(speedrunRecord.getDate())
                .build();
    }

    public void createSpeedrunRecord(SpeedrunRequest speedrunRequest){
        SpeedrunRecord speedrunRecord = SpeedrunRecord.builder()
                .userId(speedrunRequest.getUserId())
                .game(speedrunRequest.getGame())
                .category(speedrunRequest.getCategory())
                .time(speedrunRequest.getTime())
                .date(speedrunRequest.getDate())
                .build();

        speedrunRepository.save(speedrunRecord);
    }

    public List<SpeedrunResponse> getAllSpeedrunRecordsByUserId(List<Integer> users){
        List<SpeedrunRecord> speedrunRecords = speedrunRepository.findByUserIdIn(users);
        return speedrunRecords.stream().map(this::mapToSpeedrunResponse).toList();
    }

    public List<SpeedrunResponse> getAllSpeedrunRecords() {
        List<SpeedrunRecord> speedruns = speedrunRepository.findAll();
        return speedruns.stream().map(this::mapToSpeedrunResponse).toList();
    }
}
