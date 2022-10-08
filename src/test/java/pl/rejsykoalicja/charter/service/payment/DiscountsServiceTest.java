package pl.rejsykoalicja.charter.service.payment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.enums.Discount;
import pl.rejsykoalicja.charter.service.CustomerService;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountsServiceTest {
    @InjectMocks
    DiscountsService service;
    @Mock
    CustomerService customerService;

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
        //given
        //when
        //then
    }

    @Test
    void shouldReturnMoreThanOneDiscount() {
        //given
        //when
        //then
    }

    @Test
    void shouldReturn0DiscountValue() {
        //given
        //when
        //then
    }

    @Test
    void shouldReturnDiscountValueForJanuaryBooking() {
        //given
        //when
        //then
    }

    @Test
    void shouldSumTwoDiscounts() {
        //given
        //when
        //then
    }

    @Test
    void shouldReturnNoMoreThanMaximumDiscount() {
        //given
        //when
        //then
    }

    @Test
    void shouldReturnDiscountFromVoucher() {
        //given
        //when
        //then
    }

    @Test
    void shouldReturnSumOfDiscountAndVoucher() {
        //given
        //when
        //then
    }

    @Test
    void shouldReturnDiscountWithVoucherNotExceededMaxValue() {
        //given
        //when
        //then
    }

    @Test
    void shouldReturnDiscountExceededMaxValue() {
        //given
        //when
        //then
    }

    @Test
    void shouldReturnDiscountExceededMaxValueOnlyByVoucher() {
        //given
        //when
        //then
    }

    private CharterDto createCharterWithoutDiscounts() {

        ZonedDateTime from = ZonedDateTime.parse("2022-05-05T15:00:00+00:00[Europe/Berlin]");
        return CharterDto.builder()
                         .from(from)
                         .to(from.plusWeeks(1))
                         .build();
    }
}
