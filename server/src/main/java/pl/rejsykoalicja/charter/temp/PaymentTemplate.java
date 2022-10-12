package pl.rejsykoalicja.charter.temp;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.util.List;

@Getter
@Builder
public class PaymentTemplate {
    private Long maxDaysToCharter;
    List<Pair<Long, Integer>> daysToPaymentsSize;

    public int getDepositSize() {
        if (daysToPaymentsSize.isEmpty()) {
            return 100;
        }
        return 100 - daysToPaymentsSize.stream().mapToInt(Pair::getSecond).sum();
    }
}
