package pl.rejsykoalicja.charter.service.payment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.rejsykoalicja.charter.Global;
import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.dto.VoucherDto;
import pl.rejsykoalicja.charter.enums.Discount;
import pl.rejsykoalicja.charter.repository.VoucherRepository;
import pl.rejsykoalicja.charter.repository.entities.Voucher;
import pl.rejsykoalicja.charter.service.CustomerService;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountsServiceTest {
    @InjectMocks
    DiscountsService service;
    @Mock
    CustomerService customerService;
    @Mock
    VoucherRepository voucherRepository;

    @BeforeAll
    static void beforeAll() {
        // avoid adding january discount when testing in january
        Clock clock = Clock.fixed(Instant.parse("2022-05-05T12:00:00.00Z"), ZoneId.of("UTC"));
        MockedStatic<Clock> mockedDateTime = Mockito.mockStatic(Clock.class);
        mockedDateTime.when(Clock::systemDefaultZone).thenReturn(clock);
    }

    @Test
    void shouldReturnTwoWeeksDiscountWhenCharterIsTwoWeeksLong() {
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(2));
        //when
        List<Discount> allDiscounts = service.getAllDiscounts(charterTemplate);
        //then
        assertThat(allDiscounts).hasSize(1);
        assertThat(allDiscounts).contains(Discount.TWO_WEEKS);
    }

    @Test
    void shouldReturnThreeWeeksDiscountWhenCharterIsThreeWeeksLong() {
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(3));
        //when
        List<Discount> allDiscounts = service.getAllDiscounts(charterTemplate);
        //then
        assertThat(allDiscounts).hasSize(1);
        assertThat(allDiscounts).contains(Discount.THREE_WEEKS);
    }

    @Test
    void shouldReturnJanuuaryDiscountWhenBookingInJanuary() {
        ZonedDateTime january = ZonedDateTime.parse("2022-01-05T15:00:00+00:00[Europe/Berlin]");
        try (MockedStatic<ZonedDateTime> mockedDateTime = Mockito.mockStatic(ZonedDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            //mock
            mockedDateTime.when(ZonedDateTime::now).thenReturn(january);
            //given
            ZonedDateTime from = ZonedDateTime.parse("2022-05-05T15:00:00+00:00[Europe/Berlin]");
            CharterDto charterTemplate = createCharterWithoutDiscounts();
            //when
            List<Discount> allDiscounts = service.getAllDiscounts(charterTemplate);
            //then
            assertThat(allDiscounts).hasSize(1);
            assertThat(allDiscounts).contains(Discount.JANUARY_BOOKING);
        }
    }

    @Test
    void shouldReturnFebruaryDiscountWhenBookingInFebruary() {
        ZonedDateTime february = ZonedDateTime.parse("2022-02-05T15:00:00+00:00[Europe/Berlin]");
        try (MockedStatic<ZonedDateTime> mockedDateTime = Mockito.mockStatic(ZonedDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            //mock
            mockedDateTime.when(ZonedDateTime::now).thenReturn(february);
            //given
            CharterDto charterTemplate = createCharterWithoutDiscounts();
            //when
            List<Discount> allDiscounts = service.getAllDiscounts(charterTemplate);
            //then
            assertThat(allDiscounts).hasSize(1);
            assertThat(allDiscounts).contains(Discount.FEBRUARY_BOOKING);
        }
    }

    @Test
    void shouldReturnRegularCustomerDiscountWhenBookingByRegularCustomer() {
        //mock
        when(customerService.isRegularCustomer()).thenReturn(true);
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        //when
        List<Discount> allDiscounts = service.getAllDiscounts(charterTemplate);
        //then
        assertThat(allDiscounts).hasSize(1);
        assertThat(allDiscounts).contains(Discount.REGULAR_CUSTOMER);
    }

    @Test
    void shouldReturnNoDiscounts() {
        //mock
        when(customerService.isRegularCustomer()).thenReturn(false);
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        //when
        List<Discount> allDiscounts = service.getAllDiscounts(charterTemplate);
        //then
        assertThat(allDiscounts).isEmpty();
    }

    @Test
    void shouldReturnMoreThanOneDiscount() {
        //mock
        when(customerService.isRegularCustomer()).thenReturn(true);
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(2));
        //when
        List<Discount> allDiscounts = service.getAllDiscounts(charterTemplate);
        //then
        assertThat(allDiscounts).hasSize(2);
        assertThat(allDiscounts).contains(Discount.REGULAR_CUSTOMER);
        assertThat(allDiscounts).contains(Discount.TWO_WEEKS);
    }

    @Test
    void shouldReturn0DiscountValue() {
        //given
        //when
        Integer discountValue = service.getDiscountValue(List.of(), null);
        //then
        assertThat(discountValue).isEqualTo(0);
    }

    @Test
    void shouldReturnDiscountValueForTwooWeeksBooking() {
        //given
        Discount discount = Discount.TWO_WEEKS;
        //when
        Integer discountValue = service.getDiscountValue(List.of(discount), null);
        //then
        assertThat(discountValue).isEqualTo(discount.discountSize);
    }

    @Test
    void shouldSumTwoDiscounts() {
        //given
        Discount discountOne = Discount.TWO_WEEKS;
        Discount discountTwo = Discount.THREE_WEEKS;
        //when
        Integer discountValue = service.getDiscountValue(List.of(discountOne, discountTwo), null);
        //then
        assertThat(discountValue).isEqualTo(discountOne.discountSize + discountTwo.discountSize);
    }

    @Test
    void shouldReturnNoMoreThanMaximumDiscount() {
        //given
        List<Discount> discounts = Arrays.asList(Discount.values());
        //when
        Integer discountValue = service.getDiscountValue(discounts, null);
        //then
        assertThat(discountValue).isEqualTo(Global.MAX_DISCOUNT);
    }

    @Test
    void shouldReturnDiscountFromVoucher() {
        //mock
        Voucher voucherInDb = Voucher.builder().amount(10).code("sample Code").canExceedLimit(false).build();
        when(voucherRepository.getByCode(voucherInDb.getCode())).thenReturn(Optional.of(voucherInDb));
        //given
        VoucherDto requestVoucher = VoucherDto.builder().code("sample Code").build();
        //when
        Integer discountValue = service.getDiscountValue(List.of(), requestVoucher);
        //then
        assertThat(discountValue).isEqualTo(voucherInDb.getAmount());
    }

    @Test
    void shouldIgnoreVoucherValueFromDto() {
        //mock
        Voucher voucherInDb = Voucher.builder().amount(10).code("sample Code").canExceedLimit(false).build();
        when(voucherRepository.getByCode(voucherInDb.getCode())).thenReturn(Optional.of(voucherInDb));
        //given
        VoucherDto requestVoucher = VoucherDto.builder().amount(60).code("sample Code").build();
        //when
        Integer discountValue = service.getDiscountValue(List.of(), requestVoucher);
        //then
        assertThat(discountValue).isNotEqualTo(requestVoucher.getAmount());
        assertThat(discountValue).isEqualTo(voucherInDb.getAmount());
    }

    @Test
    void shouldReturnSumOfDiscountAndVoucher() {
        //mock
        Voucher voucherInDb = Voucher.builder().amount(10).code("sample Code").canExceedLimit(false).build();
        when(voucherRepository.getByCode(voucherInDb.getCode())).thenReturn(Optional.of(voucherInDb));
        //given
        VoucherDto requestVoucher = VoucherDto.builder().code("sample Code").build();
        Discount discount = Discount.TWO_WEEKS;
        //when
        Integer discountValue = service.getDiscountValue(List.of(discount), requestVoucher);
        //then
        assertThat(discountValue).isEqualTo(voucherInDb.getAmount() + discount.discountSize);
    }

    @Test
    void shouldReturnDiscountWithVoucherNotExceededMaxValue() {
        //mock
        Voucher voucherInDb =
                Voucher.builder().amount(Global.MAX_DISCOUNT - 1).code("sample Code").canExceedLimit(false).build();
        when(voucherRepository.getByCode(voucherInDb.getCode())).thenReturn(Optional.of(voucherInDb));
        //given
        VoucherDto requestVoucher = VoucherDto.builder().code("sample Code").build();
        Discount discount = Discount.TWO_WEEKS;
        //when
        Integer discountValue = service.getDiscountValue(List.of(discount), requestVoucher);
        //then
        assertThat(discountValue).isEqualTo(Global.MAX_DISCOUNT);
    }

    @Test
    void shouldReturnDiscountExceededMaxValue() {
        //mock
        Voucher voucherInDb =
                Voucher.builder().amount(Global.MAX_DISCOUNT - 1).code("sample Code").canExceedLimit(true).build();
        when(voucherRepository.getByCode(voucherInDb.getCode())).thenReturn(Optional.of(voucherInDb));
        //given
        VoucherDto requestVoucher = VoucherDto.builder().code("sample Code").build();
        Discount discount = Discount.TWO_WEEKS;
        //when
        Integer discountValue = service.getDiscountValue(List.of(discount), requestVoucher);
        //then
        assertThat(discountValue).isEqualTo(voucherInDb.getAmount() + discount.discountSize);
    }

    @Test
    void shouldReturnDiscountExceededMaxValueOnlyByVoucherValue() {
        //mock
        Voucher voucherInDb =
                Voucher.builder().amount(11).code("sample Code").canExceedLimit(true).build();
        when(voucherRepository.getByCode(voucherInDb.getCode())).thenReturn(Optional.of(voucherInDb));
        //given
        VoucherDto requestVoucher = VoucherDto.builder().code("sample Code").build();
        //when
        Integer discountValue = service.getDiscountValue(Arrays.asList(Discount.values()), requestVoucher);
        //then
        assertThat(discountValue).isEqualTo(voucherInDb.getAmount() + Global.MAX_DISCOUNT);
    }

    private CharterDto createCharterWithoutDiscounts() {

        ZonedDateTime from = ZonedDateTime.parse("2022-05-05T15:00:00+00:00[Europe/Berlin]");
        return CharterDto.builder()
                         .from(from)
                         .to(from.plusWeeks(1))
                         .build();
    }
}
