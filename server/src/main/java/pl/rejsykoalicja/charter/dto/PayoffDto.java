package pl.rejsykoalicja.charter.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.util.Pair;
import pl.rejsykoalicja.charter.enums.Discount;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PayoffDto {
    private BigDecimal totalCost;
    private List<Pair<Long, BigDecimal>> days2Price;
    private Integer discountValue;
    private List<Discount> discounts;
    private VoucherDto voucher;
    private List<PaymentDto> payments;
}
