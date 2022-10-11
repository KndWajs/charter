package pl.rejsykoalicja.charter.service;

import lombok.AllArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class CalendarService {

    Environment env;

    public List<ZonedDateTime> getCalendar() {
        throw new NotYetImplementedException();
    }

    public boolean isAvailable(ZonedDateTime from, ZonedDateTime to) {
        if (Arrays.asList(env.getActiveProfiles()).contains("dev") ||
                Arrays.asList(env.getActiveProfiles()).contains("test")) {
            return true;
        }
        throw new NotYetImplementedException();
    }
}
