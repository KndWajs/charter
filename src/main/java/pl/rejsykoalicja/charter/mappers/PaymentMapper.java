package pl.rejsykoalicja.charter.mappers;

import pl.rejsykoalicja.charter.dto.PaymentDto;
import pl.rejsykoalicja.charter.repository.entities.Payment;

public class PaymentMapper {

    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                         .amount(payment.getAmount())
                         .paid(payment.getPaid())
                         .paymentDate(payment.getPaymentDate())
                         .tag(payment.getTag())
                         .build();
    }
}
