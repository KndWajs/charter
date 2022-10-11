package pl.rejsykoalicja.charter.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.rejsykoalicja.charter.service.CalendarService;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/data")
@AllArgsConstructor
public class FrontEndDataController {

    private final CalendarService calendarService;

    @GetMapping(value = "/calendar")
    public List<ZonedDateTime> getCalendar() {
        return calendarService.getCalendar();
    }

    @GetMapping(value = "/isAvailable")
    public boolean checkAvailability
            (@RequestParam(name = "date-from") ZonedDateTime from, @RequestParam(name = "date-to") ZonedDateTime to) {
        return calendarService.isAvailable(from, to);
    }

}
