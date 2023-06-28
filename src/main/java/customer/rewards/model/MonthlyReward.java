package customer.rewards.model;

import lombok.Builder;
import lombok.Data;

import java.time.Month;
import java.time.OffsetDateTime;

@Data
@Builder(toBuilder = true)
public class MonthlyReward {
    private final String month;
    private final Integer points;
}
