package customer.rewards.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class Rewards {
    private final String customerId;
    private final Integer points;
    private final List<MonthlyReward> monthlyRewards;
}
