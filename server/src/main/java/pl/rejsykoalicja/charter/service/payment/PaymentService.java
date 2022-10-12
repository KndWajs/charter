package pl.rejsykoalicja.charter.service.payment;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.rejsykoalicja.charter.Global;
import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.dto.PaymentDto;
import pl.rejsykoalicja.charter.dto.PayoffDto;
import pl.rejsykoalicja.charter.enums.Discount;
import pl.rejsykoalicja.charter.mappers.VoucherMapper;
import pl.rejsykoalicja.charter.repository.VoucherRepository;
import pl.rejsykoalicja.charter.repository.entities.Voucher;
import pl.rejsykoalicja.charter.temp.PaymentTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
        List<Pair<Long, BigDecimal>> days2Price = calculateDays2Price(charter);
        BigDecimal totalCost = calculateTotalCost(days2Price, totalDiscount);

        return PayoffDto.builder()
                        .discounts(allDiscounts)
                        .voucher(VoucherMapper.toDto(voucher))
                        .discountValue(totalDiscount)
                        .days2Price(days2Price)
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

    private List<Pair<Long, BigDecimal>> calculateDays2Price(CharterDto charter) {
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

        return days2Price;
    }

    private BigDecimal calculateTotalCost(List<Pair<Long, BigDecimal>> days2Price, Integer totalDiscount) {

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

        PaymentTemplate paymentTemplate = getPaymentTemplate(daysToCharter);

        List<Pair<Long, Integer>> payments = new ArrayList<>();
        payments.add(Pair.of(daysToCharter, paymentTemplate.getDepositSize()));
        paymentTemplate.getDaysToPaymentsSize().forEach(d2p -> payments.add(Pair.of(d2p.getFirst(), d2p.getSecond())));
        return payments;
    }

    private PaymentTemplate getPaymentTemplate(long daysToCharter) {
        List<PaymentTemplate> paymentTemplates = Global.paymentTemplates;
        paymentTemplates.sort(Comparator.comparingLong(PaymentTemplate::getMaxDaysToCharter));

        PaymentTemplate paymentTemplate = paymentTemplates.get(0);

        for (int i = 0; paymentTemplates.get(i).getMaxDaysToCharter() <= daysToCharter; i++) {
            paymentTemplate = paymentTemplates.get(i + 1);
        }

        return paymentTemplate;
    }

    private String getPaymentTag(ZonedDateTime from, int number) {
        return String.format("%s-%s-%s-%s", from.getYear(), from.getMonthValue(), from.getDayOfMonth(), number);
    }
}
