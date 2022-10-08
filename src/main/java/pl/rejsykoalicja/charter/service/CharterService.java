package pl.rejsykoalicja.charter.service;

import lombok.AllArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;
import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.exceptions.KnownException;
import pl.rejsykoalicja.charter.mappers.CharterMapper;
import pl.rejsykoalicja.charter.repository.CharterRepository;
import pl.rejsykoalicja.charter.service.payment.PaymentService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CharterService {
    private CharterRepository charterRepository;
    private ThermsService thermsService;
    private PaymentService paymentService;
    private CalendarService calendarService;

    public List<CharterDto> getAll() {
        return charterRepository.findAll().stream().map(CharterMapper::toDto).collect(Collectors.toList());
    }

    public CharterDto createCharter(CharterDto dto) {
        validateCharter(dto);

        return CharterDto.builder()
                         .equipment(dto.getEquipment())
                         .from(dto.getFrom())
                         .to(dto.getTo())
                         .thermsAndConditionVersion(thermsService.getLatestTherms())
                         .payoff(paymentService.getPayoff(dto))
                         .build();
    }

    private void validateCharter(CharterDto dto) {
        if (dto.getFrom() == null || dto.getTo() == null) {
            throw new KnownException("Missing date");
        }

        if (calendarService.checkAvailability(dto.getFrom(), dto.getTo())) {
            throw new KnownException("This date is already booked");
        }

    }

    public CharterDto save(CharterDto dto) {
        throw new NotYetImplementedException();
    }

    public CharterDto update(CharterDto dto) {
        throw new NotYetImplementedException();
    }

    public void deleteById(BigInteger id) {
        throw new NotYetImplementedException();
    }

    public byte[] getThermsAndConditions(String version) throws IOException {
        throw new NotYetImplementedException();
    }

    public byte[] getCharterAgreement(BigInteger charterId) throws IOException {
        throw new NotYetImplementedException();
    }
}
