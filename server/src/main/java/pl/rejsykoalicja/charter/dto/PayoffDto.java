package pl.rejsykoalicja.charter.dto;

import lombok.Builder;
import lombok.Data;
import pl.rejsykoalicja.charter.enums.Discount;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PayoffDto {
    private BigDecimal totalCost;
    private Integer discountValue;
    private List<Discount> discounts;
    private VoucherDto voucher;
    private List<PaymentDto> payments;
}
