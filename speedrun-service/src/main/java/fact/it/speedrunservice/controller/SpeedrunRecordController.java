package fact.it.speedrunservice.controller;

import fact.it.speedrunservice.dto.SpeedrunRequest;
import fact.it.speedrunservice.dto.SpeedrunResponse;
import fact.it.speedrunservice.service.SpeedrunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speedrun")
@RequiredArgsConstructor
public class SpeedrunRecordController {
    private final SpeedrunService speedrunService; //service layer

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createSpeedrunRecord(@RequestBody SpeedrunRequest speedrunRequest) {
        speedrunService.createSpeedrunRecord(speedrunRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SpeedrunResponse> getAllSpeedrunRecordsByUserId(@RequestParam List<Integer> users){
        return speedrunService.getAllSpeedrunRecordsByUserId(users);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<SpeedrunResponse> getAllSpeedrunRecords(){
        return speedrunService.getAllSpeedrunRecords();
    }
}
