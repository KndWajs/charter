package pl.rejsykoalicja.charter.service.payment;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.rejsykoalicja.charter.Global;
import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.dto.PaymentDto;
import pl.rejsykoalicja.charter.dto.PayoffDto;
import pl.rejsykoalicja.charter.dto.VoucherDto;
import pl.rejsykoalicja.charter.enums.Discount;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PaymentService {
    private DiscountsService discountsService;

    public PayoffDto getPayoff(CharterDto charter) {
        List<Discount> allDiscounts = discountsService.getAllDiscounts(charter);

        VoucherDto voucher = null;
        if (charter.getPayoff() != null) {
            voucher = charter.getPayoff().getVoucher();
        }

        Integer totalDiscount = discountsService.getDiscountValue(allDiscounts, voucher);
        BigDecimal totalCost = calculateTotalCost(totalDiscount, charter);
        return PayoffDto.builder()
                        .discounts(allDiscounts)
                        .voucher(charter.getPayoff().getVoucher())
                        .discountValue(totalDiscount)
                        .totalCost(totalCost)
                        .payments(createPayments(charter.getFrom(), totalCost))
                        .build();
    }

    private BigDecimal calculateTotalCost(Integer totalDiscount, CharterDto charter) {
        int startDay = charter.getFrom().getDayOfYear();
        int endDay = charter.getTo().getDayOfYear();
        BigDecimal price;
        List<Pair<Integer, BigDecimal>> days2Price = new ArrayList<>();
        int countDays = 0;

        for (Map.Entry<ZonedDateTime, BigDecimal> season : Global.SEASON_PRICES.entrySet()) {
            price = season.getValue();
            //because of leap year needed to update year in season enum
            int seasonStartDay = season.getKey().withYear(charter.getFrom().getYear()).getDayOfYear();
            int days = 0;
            if (startDay < seasonStartDay) {
                days = seasonStartDay - startDay - countDays;
            }
            if (endDay < seasonStartDay) {
                days2Price.add(Pair.of(days - (seasonStartDay - endDay), price));
                break;
            }
            if (days > 0) {
                countDays += days;
                days2Price.add(Pair.of(days, price));
            }
        }

        return days2Price.stream().map(pair -> pair.getSecond().multiply(new BigDecimal(pair.getFirst())))
                         .reduce(new BigDecimal(0), (subtotal, cost) -> cost.add(subtotal))
                         .multiply(BigDecimal.valueOf((100 - totalDiscount) / 100d));
    }

    private List<PaymentDto> createPayments(ZonedDateTime from, BigDecimal totalCost) {

        List<PaymentDto> payments = new ArrayList<>();

        for (Pair<Integer, Integer> days2part : getPaymentsSizeInPercent(from)) {
            payments.add(PaymentDto.builder()
                                   .amount(totalCost.multiply(BigDecimal.valueOf(days2part.getSecond() / 100d)))
                                   .paid(false)
                                   .paymentDate(from.minusDays(days2part.getFirst()))
                                   .tag(getPaymentTag(from, days2part.getFirst()))
                                   .build());
        }

        return payments;
    }

    private List<Pair<Integer, Integer>> getPaymentsSizeInPercent(ZonedDateTime from) {
        int daysToCharter = from.getDayOfYear() - ZonedDateTime.now().getDayOfYear();
        if (daysToCharter > 30) {
            return List.of(Pair.of(daysToCharter, 30), Pair.of(30, 20), Pair.of(15, 50));
        } else if (daysToCharter > 15) {
            return List.of(Pair.of(daysToCharter, 50), Pair.of(15, 50));
        }
        return List.of(Pair.of(daysToCharter, 100));
    }

    private String getPaymentTag(ZonedDateTime from, int number) {
        return String.format("%s-%s-%s-%s", from.getYear(), from.getMonthValue(), from.getDayOfMonth(), number);
    }
}
