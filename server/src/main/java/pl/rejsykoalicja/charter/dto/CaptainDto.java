package pl.rejsykoalicja.charter.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class CaptainDto {
    private String firstName;
    private String lastName;
    private ZonedDateTime birthDate;
    private String licenseNo;
}
