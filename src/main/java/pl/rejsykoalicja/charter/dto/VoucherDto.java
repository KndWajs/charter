package pl.rejsykoalicja.charter.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoucherDto {
    private String name;
    private String code;
    private Integer amount;
}
