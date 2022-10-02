package pl.rejsykoalicja.charter.service;

import lombok.AllArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;
import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.mappers.CharterMapper;
import pl.rejsykoalicja.charter.repository.CharterRepository;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CharterService {
    private CharterRepository charterRepository;

    public List<CharterDto> getAll() {
        return charterRepository.findAll().stream().map(CharterMapper::toDto).collect(Collectors.toList());
    }

    public CharterDto createSample(CharterDto dto) {
        throw new NotYetImplementedException();
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
