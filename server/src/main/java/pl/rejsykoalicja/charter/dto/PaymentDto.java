package pl.rejsykoalicja.charter.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class PaymentDto {
    private BigDecimal amount;
    private ZonedDateTime paymentDate;
    private Boolean paid;
    private String tag;
}
