package pl.rejsykoalicja.charter.mappers;

import pl.rejsykoalicja.charter.dto.PayoffDto;
import pl.rejsykoalicja.charter.repository.entities.Payoff;

import java.util.stream.Collectors;

public class PayoffMapper {

    public static PayoffDto toDto(Payoff payoff) {
        return PayoffDto.builder()
                        .discounts(payoff.getDiscounts())
                        .discountValue(payoff.getDiscountValue())
                        .payments(payoff.getPayments().stream().map(PaymentMapper::toDto).collect(Collectors.toList()))
                        .totalCost(payoff.getTotalCost())
                        .voucher(VoucherMapper.toDto(payoff.getVoucher()))
                        .build();
    }
}
