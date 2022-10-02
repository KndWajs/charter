package pl.rejsykoalicja.charter.dto;

import lombok.Builder;
import pl.rejsykoalicja.charter.enums.Equipment;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public class CharterDto {
    private List<Equipment> equipment;
    private ZonedDateTime from;
    private ZonedDateTime to;
    private CaptainDto captain;
    private Integer crewNumber;
    private PayoffDto payoff;
    private ZonedDateTime reservation;
    private String thermsAndConditionVersion;
}
