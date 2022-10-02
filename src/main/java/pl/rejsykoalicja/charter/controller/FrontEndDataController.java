package pl.rejsykoalicja.charter.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.rejsykoalicja.charter.service.FrontEndDataService;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/data")
@AllArgsConstructor
public class FrontEndDataController {

    private final FrontEndDataService frontEndDataService;

    @GetMapping(value = "/calendar")
    public List<ZonedDateTime> getCalendar() {
        return frontEndDataService.getCallendar();
    }

    @GetMapping(value = "/isAvailable")
    public boolean checkAvailability(@RequestParam(name = "date") ZonedDateTime zonedDateTime) {
        return frontEndDataService.checkAvailability(zonedDateTime);
    }

}
