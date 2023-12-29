package fact.it.recordservice.controller;

import fact.it.recordservice.dto.RecordRequest;
import fact.it.recordservice.dto.RecordResponse;
import fact.it.recordservice.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String createRecord(@RequestBody RecordRequest recordRequest){
        boolean result = recordService.createOrUpdateRecord(null, recordRequest);
        return (result ? "Record created succesfully" : "Record failed to create");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RecordResponse> getAllRecordsOfGame(@RequestParam String gameName){
        return recordService.getAllRecordsOfGame(gameName);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RecordResponse> getAllRecords(){
        return recordService.getAllRecords();
    }

    @PutMapping("/{recordNumber}")
    @ResponseStatus(HttpStatus.OK)
    public String updateRecord(@PathVariable String recordNumber, @RequestBody RecordRequest recordRequest) {
        boolean result = recordService.createOrUpdateRecord(recordNumber, recordRequest);
        return (result ? "Record updated succesfully" : "Record update failed");
    }

    @DeleteMapping("/{recordNumber}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRecord(@PathVariable String recordNumber) {
        boolean result = recordService.deleteRecord(recordNumber);
        return (result ? "Record deleted succesfully" : "Record deletion failed");
    }
}
