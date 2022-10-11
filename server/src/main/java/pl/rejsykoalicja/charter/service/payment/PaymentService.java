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
import pl.rejsykoalicja.charter.mappers.VoucherMapper;
import pl.rejsykoalicja.charter.repository.VoucherRepository;
import pl.rejsykoalicja.charter.repository.entities.Voucher;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PaymentService {
    private DiscountsService discountsService;
    private VoucherRepository voucherRepository;

    public PayoffDto getPayoff(CharterDto charter) {
        List<Discount> allDiscounts = discountsService.getAllDiscounts(charter);

        Voucher voucher = getVoucher(charter);

        Integer totalDiscount = discountsService.getDiscountValue(allDiscounts, voucher);
        BigDecimal totalCost = calculateTotalCost(totalDiscount, charter);
        return PayoffDto.builder()
                        .discounts(allDiscounts)
                        .voucher(VoucherMapper.toDto(voucher))
                        .discountValue(totalDiscount)
                        .totalCost(totalCost)
                        .payments(createPayments(charter.getFrom(), totalCost))
                        .build();
    }

    private Voucher getVoucher(CharterDto charter) {
        Voucher emptyVoucher = Voucher.builder().amount(0).canExceedLimit(false).build();
        if (charter.getPayoff() == null || charter.getPayoff().getVoucher() == null) {
            return emptyVoucher;
        } else {
            return voucherRepository.getByCode(charter.getPayoff().getVoucher().getCode()).orElse(emptyVoucher);
        }
    }

    private BigDecimal calculateTotalCost(Integer totalDiscount, CharterDto charter) {
        //TODO calculate with extra equipment
        ZonedDateTime startDay = charter.getFrom();
        ZonedDateTime endDay = charter.getTo();
        BigDecimal price;
        List<Pair<Long, BigDecimal>> days2Price = new ArrayList<>();
        int countDays = 0;

        for (Map.Entry<ZonedDateTime, BigDecimal> season : Global.SEASON_PRICES.entrySet()) {
            price = season.getValue();
            //because of leap year needed to update year in season enum
            ZonedDateTime seasonStartDay = season.getKey().withYear(charter.getFrom().getYear());
            int days = 0;
            if (startDay.isBefore(seasonStartDay)) {
                days = (int) Duration.between(startDay, seasonStartDay).minusDays(countDays).toDays();
            }
            if (endDay.isBefore(seasonStartDay)) {
                days2Price.add(Pair.of(days - Duration.between(endDay, seasonStartDay).toDays(), price));
                break;
            }
            if (days > 0) {
                countDays += days;
                days2Price.add(Pair.of((long) days, price));
            }
        }

        return days2Price.stream().map(pair -> pair.getSecond().multiply(new BigDecimal(pair.getFirst())))
                         .reduce(new BigDecimal(0), (subtotal, cost) -> cost.add(subtotal))
                         .multiply(BigDecimal.valueOf((100 - totalDiscount) / 100d));
    }

    private List<PaymentDto> createPayments(ZonedDateTime from, BigDecimal totalCost) {

        List<PaymentDto> payments = new ArrayList<>();

        for (Pair<Long, Integer> days2part : getDaysToPaymentsSizeInPercent(from)) {
            payments.add(PaymentDto.builder()
                                   .amount(totalCost.multiply(BigDecimal.valueOf(days2part.getSecond() / 100d)))
                                   .paid(false)
                                   .paymentDate(from.minusDays(days2part.getFirst()))
                                   .tag(getPaymentTag(from, days2part.getFirst().intValue()))
                                   .build());
        }

        return payments;
    }

    private List<Pair<Long, Integer>> getDaysToPaymentsSizeInPercent(ZonedDateTime charterStart) {
        long daysToCharter = Duration.between(ZonedDateTime.now(), charterStart).toDays();

        if (daysToCharter > 30) {
            return List.of(Pair.of(daysToCharter, 30), Pair.of(30L, 20), Pair.of(15L, 50));
        } else if (daysToCharter > 15) {
            return List.of(Pair.of(daysToCharter, 50), Pair.of(15L, 50));
        }
        return List.of(Pair.of(daysToCharter, 100));
    }

    private String getPaymentTag(ZonedDateTime from, int number) {
        return String.format("%s-%s-%s-%s", from.getYear(), from.getMonthValue(), from.getDayOfMonth(), number);
    }
}
