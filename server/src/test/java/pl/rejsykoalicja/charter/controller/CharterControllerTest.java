package pl.rejsykoalicja.charter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.rejsykoalicja.charter.Global;
import pl.rejsykoalicja.charter.dto.CharterDto;
import pl.rejsykoalicja.charter.dto.PayoffDto;
import pl.rejsykoalicja.charter.dto.VoucherDto;
import pl.rejsykoalicja.charter.enums.Discount;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
class CharterControllerTest {

    //TODO split test and dev sql data

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper MAPPER = new JsonMapper();

    static MockedStatic<Clock> mockedDateTime;

    @BeforeAll
    static void beforeAll() {
        // avoid adding january discount when testing in january
        Clock clock = Clock.fixed(Instant.parse("2022-05-05T12:00:00.00Z"), ZoneId.of("UTC"));
        mockedDateTime = Mockito.mockStatic(Clock.class, InvocationOnMock::callRealMethod);
        mockedDateTime.when(Clock::systemDefaultZone).thenReturn(clock);
    }

    @BeforeEach
    void setUp() {
        MAPPER.findAndRegisterModules();
    }

    @AfterAll
    static void afterAll() {
        mockedDateTime.close();
    }

    @Test
    void shouldReturnCorrectPrice() throws Exception {
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getTotalCost())
                .isEqualTo(Global.SEASON_PRICES.values().stream().findFirst().orElseThrow().multiply(BigDecimal.valueOf(7d)));
        assertThat(responseCharterDto.getPayoff().getDiscountValue()).isEqualTo(0);
    }

    @Test
    void shouldReturnTwoWeeksDiscountWhenCharterIsTwoWeeksLong() throws Exception {
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(2));
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscounts()).hasSize(1);
        assertThat(responseCharterDto.getPayoff().getDiscounts()).contains(Discount.TWO_WEEKS);
    }

    @Test
    void shouldReturnThreeWeeksDiscountWhenCharterIsThreeWeeksLong() throws Exception {
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(3));
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscounts()).hasSize(1);
        assertThat(responseCharterDto.getPayoff().getDiscounts()).contains(Discount.THREE_WEEKS);
    }

    @Test
    void shouldReturnJanuuaryDiscountWhenBookingInJanuary() throws Exception {
        ZonedDateTime january = ZonedDateTime.parse("2022-01-05T15:00:00+00:00[Europe/Berlin]");
        try (MockedStatic<ZonedDateTime> mockedDateTime = Mockito.mockStatic(ZonedDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            //mock
            mockedDateTime.when(ZonedDateTime::now).thenReturn(january);
            //given
            ZonedDateTime from = ZonedDateTime.parse("2022-05-05T15:00:00+00:00[Europe/Berlin]");
            CharterDto charterTemplate = createCharterWithoutDiscounts();
            //when
            MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
            CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
            //then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(responseCharterDto.getPayoff().getDiscounts()).hasSize(1);
            assertThat(responseCharterDto.getPayoff().getDiscounts()).contains(Discount.JANUARY_BOOKING);
        }
    }

    @Test
    void shouldReturnFebruaryDiscountWhenBookingInFebruary() throws Exception {
        ZonedDateTime february = ZonedDateTime.parse("2022-02-05T15:00:00+00:00[Europe/Berlin]");
        try (MockedStatic<ZonedDateTime> mockedDateTime = Mockito.mockStatic(ZonedDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            //mock
            mockedDateTime.when(ZonedDateTime::now).thenReturn(february);
            //given
            CharterDto charterTemplate = createCharterWithoutDiscounts();
            //when
            MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
            CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
            //then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(responseCharterDto.getPayoff().getDiscounts()).hasSize(1);
            assertThat(responseCharterDto.getPayoff().getDiscounts()).contains(Discount.FEBRUARY_BOOKING);
        }
    }

    @Disabled
    @Test
    void shouldReturnRegularCustomerDiscountWhenBookingByRegularCustomer() throws Exception {
        //given
        //TODO use regular customer
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscounts()).hasSize(1);
        assertThat(responseCharterDto.getPayoff().getDiscounts()).contains(Discount.REGULAR_CUSTOMER);
    }

    @Test
    void shouldReturnNoDiscounts() throws Exception {
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscounts()).isEmpty();
    }

    @Test
    void shouldReturnMoreThanOneDiscount() throws Exception {
        ZonedDateTime february = ZonedDateTime.parse("2022-02-05T15:00:00+00:00[Europe/Berlin]");
        try (MockedStatic<ZonedDateTime> mockedDateTime = Mockito.mockStatic(ZonedDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            //mock
            mockedDateTime.when(ZonedDateTime::now).thenReturn(february);
            //given
            CharterDto charterTemplate = createCharterWithoutDiscounts();
            charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(2));
            //when
            MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
            CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
            //then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(responseCharterDto.getPayoff().getDiscounts()).hasSize(2);
            assertThat(responseCharterDto.getPayoff().getDiscounts()).contains(Discount.FEBRUARY_BOOKING);
            assertThat(responseCharterDto.getPayoff().getDiscounts()).contains(Discount.TWO_WEEKS);
        }
    }

    @Test
    void shouldReturn0DiscountValue() throws Exception {
        //given
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscountValue()).isEqualTo(0);
    }

    @Test
    void shouldReturnDiscountValueForTwoWeeksBooking() throws Exception {
        //given
        Discount discount = Discount.TWO_WEEKS;
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(2));
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscountValue()).isEqualTo(discount.discountSize);
    }

    @Test
    void shouldSumTwoDiscounts() throws Exception {
        ZonedDateTime february = ZonedDateTime.parse("2022-02-05T15:00:00+00:00[Europe/Berlin]");
        try (MockedStatic<ZonedDateTime> mockedDateTime = Mockito.mockStatic(ZonedDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            //mocked
            mockedDateTime.when(ZonedDateTime::now).thenReturn(february);
            //given
            Discount discountOne = Discount.FEBRUARY_BOOKING;
            Discount discountTwo = Discount.THREE_WEEKS;
            CharterDto charterTemplate = createCharterWithoutDiscounts();
            charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(3));
            //when
            MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
            CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
            //then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(responseCharterDto.getPayoff().getDiscountValue())
                    .isEqualTo(discountOne.discountSize + discountTwo.discountSize);
        }
    }

    @Test
    void shouldReturnNoMoreThanMaximumDiscount() throws Exception {
        ZonedDateTime january = ZonedDateTime.parse("2022-01-05T15:00:00+00:00[Europe/Berlin]");
        try (MockedStatic<ZonedDateTime> mockedDateTime = Mockito.mockStatic(ZonedDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            //mocked
            mockedDateTime.when(ZonedDateTime::now).thenReturn(january);
            //given
            CharterDto charterTemplate = createCharterWithoutDiscounts();
            charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(3));
            //when
            MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
            CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
            //then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(responseCharterDto.getPayoff().getDiscountValue()).isEqualTo(Global.MAX_DISCOUNT);
        }
    }

    @Test
    void shouldReturnDiscountFromVoucher() throws Exception {
        //given
        VoucherDto requestVoucher = VoucherDto.builder().code("SCD-001").build();
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setPayoff(PayoffDto.builder().voucher(requestVoucher).build());
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscountValue()).isEqualTo(10);
    }

    @Test
    void shouldIgnoreVoucherValueFromDto() throws Exception {
        //given
        VoucherDto requestVoucher = VoucherDto.builder().amount(60).code("SCD-001").build();
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setPayoff(PayoffDto.builder().voucher(requestVoucher).build());
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscountValue()).isNotEqualTo(requestVoucher.getAmount());
        assertThat(responseCharterDto.getPayoff().getDiscountValue()).isEqualTo(10);
    }

    @Test
    void shouldReturnSumOfDiscountAndVoucher() throws Exception {
        //given
        VoucherDto requestVoucher = VoucherDto.builder().code("SCD-001").build();
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setPayoff(PayoffDto.builder().voucher(requestVoucher).build());
        charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(2));
        Discount discount = Discount.TWO_WEEKS;
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscountValue())
                .isEqualTo(10 + discount.discountSize);
    }

    @Test
    void shouldReturnDiscountWithVoucherNotExceededMaxValue() throws Exception {
        ZonedDateTime january = ZonedDateTime.parse("2022-01-05T15:00:00+00:00[Europe/Berlin]");
        try (MockedStatic<ZonedDateTime> mockedDateTime = Mockito.mockStatic(ZonedDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            //mocked
            mockedDateTime.when(ZonedDateTime::now).thenReturn(january);
            //given
            VoucherDto requestVoucher = VoucherDto.builder().code("SCD-001").build();
            CharterDto charterTemplate = createCharterWithoutDiscounts();
            charterTemplate.setPayoff(PayoffDto.builder().voucher(requestVoucher).build());
            charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(3));
            //when
            MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
            CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
            //then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(responseCharterDto.getPayoff().getDiscountValue())
                    .isEqualTo(Global.MAX_DISCOUNT);
        }
    }

    @Test
    void shouldReturnDiscountExceededMaxValue() throws Exception {
        //given
        VoucherDto requestVoucher = VoucherDto.builder().code("SCD-002").build();
        CharterDto charterTemplate = createCharterWithoutDiscounts();
        charterTemplate.setPayoff(PayoffDto.builder().voucher(requestVoucher).build());
        charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(3));
        Discount discount = Discount.THREE_WEEKS;
        //when
        MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
        CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseCharterDto.getPayoff().getDiscountValue())
                .isEqualTo(19 + discount.discountSize);
    }

    @Test
    void shouldReturnDiscountExceededMaxValueOnlyByVoucherValue() throws Exception {
        ZonedDateTime january = ZonedDateTime.parse("2022-01-05T15:00:00+00:00[Europe/Berlin]");
        try (MockedStatic<ZonedDateTime> mockedDateTime = Mockito.mockStatic(ZonedDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            //mocked
            mockedDateTime.when(ZonedDateTime::now).thenReturn(january);
            //given
            VoucherDto requestVoucher = VoucherDto.builder().code("SCD-002").build();
            CharterDto charterTemplate = createCharterWithoutDiscounts();
            charterTemplate.setPayoff(PayoffDto.builder().voucher(requestVoucher).build());
            charterTemplate.setTo(charterTemplate.getFrom().plusWeeks(3));
            //when
            MockHttpServletResponse response = callPut(charterTemplate, "/api/charter/create-charter");
            CharterDto responseCharterDto = MAPPER.readValue(response.getContentAsString(), CharterDto.class);
            //then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(responseCharterDto.getPayoff().getDiscountValue())
                    .isEqualTo(19 + Global.MAX_DISCOUNT);
        }
    }

    private CharterDto createCharterWithoutDiscounts() {
        ZonedDateTime from = ZonedDateTime.parse("2022-03-05T15:00:00+00:00[Europe/Berlin]");
        return CharterDto.builder()
                         .from(from)
                         .to(from.plusWeeks(1))
                         .build();
    }

    private MockHttpServletResponse callPut(CharterDto charterTemplate, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                              .put(url)
                              .with(SecurityMockMvcRequestPostProcessors.csrf())
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(MAPPER.writeValueAsString(charterTemplate)))
                      .andReturn().getResponse();
    }
}
