package pl.rejsykoalicja.charter.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PaymentDto {
    private BigDecimal amount;
    private ZonedDateTime paymentDate;
    private Boolean paid;
    private String tag;
}
