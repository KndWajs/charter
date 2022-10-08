package pl.rejsykoalicja.charter.service.payment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rejsykoalicja.charter.Global;
import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.dto.VoucherDto;
import pl.rejsykoalicja.charter.enums.Discount;
import pl.rejsykoalicja.charter.repository.VoucherRepository;
import pl.rejsykoalicja.charter.repository.entities.Voucher;
import pl.rejsykoalicja.charter.service.CustomerService;

import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static pl.rejsykoalicja.charter.enums.Discount.FEBRUARY_BOOKING;
import static pl.rejsykoalicja.charter.enums.Discount.JANUARY_BOOKING;
import static pl.rejsykoalicja.charter.enums.Discount.REGULAR_CUSTOMER;
import static pl.rejsykoalicja.charter.enums.Discount.THREE_WEEKS;
import static pl.rejsykoalicja.charter.enums.Discount.TWO_WEEKS;

@Service
@AllArgsConstructor
public class DiscountsService {
    private CustomerService customerService;
    private VoucherRepository voucherRepository;

    List<Discount> getAllDiscounts(CharterDto dto) {
        List<Discount> discounts = new ArrayList<>();

        List<Optional<Discount>> conditions = List.of(januaryCharter(), longCharter(dto), regularCustomer(dto));

        for (Optional<Discount> discount : conditions) {
            discount.ifPresent(discounts::add);
        }

        return discounts;
    }

    Integer getDiscountValue(List<Discount> allDiscounts, VoucherDto voucherDto) {
        Voucher voucher = voucherRepository.getByCode(voucherDto.getCode())
                                           .orElse(Voucher.builder().amount(0).canExceedLimit(false).build());

        Integer discount = allDiscounts.stream().mapToInt(d -> d.discountSize).sum();
        Integer discAndVoucher = discount + voucher.getAmount();

        if (voucher.isCanExceedLimit()) {
            return discount > Global.MAX_DISCOUNT ? Global.MAX_DISCOUNT + voucher.getAmount() : discAndVoucher;
        }
        return discAndVoucher > Global.MAX_DISCOUNT ? Global.MAX_DISCOUNT : discAndVoucher;
    }

    private Optional<Discount> januaryCharter() {
        if (ZonedDateTime.now().getMonth().equals(Month.JANUARY)) {
            return Optional.of(JANUARY_BOOKING);
        } else if (ZonedDateTime.now().getMonth().equals(Month.FEBRUARY)) {
            return Optional.of(FEBRUARY_BOOKING);
        }
        return Optional.empty();
    }

    private Optional<Discount> longCharter(CharterDto dto) {
        if (dto.getTo().getDayOfYear() - dto.getFrom().getDayOfYear() >= 21) {
            return Optional.of(THREE_WEEKS);
        } else if (dto.getTo().getDayOfYear() - dto.getFrom().getDayOfYear() >= 14) {
            return Optional.of(TWO_WEEKS);
        }
        return Optional.empty();
    }

    private Optional<Discount> regularCustomer(CharterDto dto) {
        return customerService.isRegularCustomer() ? Optional.of(REGULAR_CUSTOMER) : Optional.empty();
    }

}