package pl.rejsykoalicja.charter.mappers;

import pl.rejsykoalicja.charter.dto.CaptainDto;
import pl.rejsykoalicja.charter.repository.entities.Captain;

public class CaptainMapper {

    public static CaptainDto toDto(Captain captain) {
        return CaptainDto.builder()
                         .firstName(captain.getFirstName())
                         .lastName(captain.getLastName())
                         .birthDate(captain.getBirthDate())
                         .licenseNo(captain.getLicenseNo())
                         .build();
    }
}
