package pl.rejsykoalicja.charter;

import org.springframework.data.util.Pair;
import pl.rejsykoalicja.charter.temp.PaymentTemplate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//TODO things that should be configurable for end user in the future
public class Global {
    public final static Integer MAX_DISCOUNT = 20;
    public static Map<ZonedDateTime, BigDecimal> SEASON_PRICES = new LinkedHashMap<>();
    public static List<PaymentTemplate> paymentTemplates = new ArrayList<>();

    static {
        SEASON_PRICES.put(ZonedDateTime.parse("2000-05-28T00:00:00+00:00[Europe/Berlin]"),
                new BigDecimal("715"));
        SEASON_PRICES.put(ZonedDateTime.parse("2000-08-27T00:00:00+00:00[Europe/Berlin]"),
                new BigDecimal("880"));
        SEASON_PRICES.put(ZonedDateTime.parse("2000-12-31T00:00:00+00:00[Europe/Berlin]"),
                new BigDecimal("715"));

        paymentTemplates.add(PaymentTemplate.builder()
                                            .maxDaysToCharter(15L)
                                            .daysToPaymentsSize(List.of())
                                            .build());
        paymentTemplates.add(PaymentTemplate.builder()
                                            .maxDaysToCharter(30L)
                                            .daysToPaymentsSize(List.of(Pair.of(15L, 50)))
                                            .build());
        paymentTemplates.add(PaymentTemplate.builder()
                                            .maxDaysToCharter(10000L)
                                            .daysToPaymentsSize(List.of(Pair.of(30L, 20), Pair.of(15L, 50)))
                                            .build());
    }

}
