package pl.rejsykoalicja.charter.service;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class CalendarService {
    public List<ZonedDateTime> getCalendar() {
        throw new NotYetImplementedException();
    }

    public boolean checkAvailability(ZonedDateTime from, ZonedDateTime to) {
        throw new NotYetImplementedException();
    }
}
