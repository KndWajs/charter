package pl.rejsykoalicja.charter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

//TODO things that should be configurable for end user in the future
public class Global {
    public final static Integer MAX_DISCOUNT = 20;
    public static Map<ZonedDateTime, BigDecimal> SEASON_PRICES = new LinkedHashMap<>();

    static {
        SEASON_PRICES.put(ZonedDateTime.parse("2000-05-28T00:00:00+00:00[Europe/Berlin]"),
                new BigDecimal("715"));
        SEASON_PRICES.put(ZonedDateTime.parse("2000-08-27T00:00:00+00:00[Europe/Berlin]"),
                new BigDecimal("880"));
        SEASON_PRICES.put(ZonedDateTime.parse("2000-12-31T00:00:00+00:00[Europe/Berlin]"),
                new BigDecimal("715"));
    }

}
