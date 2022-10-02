package pl.rejsykoalicja.charter.service;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class FrontEndDataService {

    public List<ZonedDateTime> getCallendar() {
        throw new NotYetImplementedException();
    }

    public boolean checkAvailability(ZonedDateTime zonedDateTime) {
        throw new NotYetImplementedException();
    }
}
