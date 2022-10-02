package pl.rejsykoalicja.charter.mappers;

import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.repository.entities.Charter;

public class CharterMapper {

    public static CharterDto toDto(Charter charter) {
        return CharterDto.builder()
                         .equipment(charter.getEquipmentList())
                         .from(charter.getFrom())
                         .to(charter.getTo())
                         .captain(CaptainMapper.toDto(charter.getCaptain()))
                         .crewNumber(charter.getCrewNumber())
                         .payoff(PayoffMapper.toDto(charter.getPayoff()))
                         .reservation(charter.getReservation())
                         .thermsAndConditionVersion(charter.getThermsAndConditionVersion())
                         .build();
    }
}
