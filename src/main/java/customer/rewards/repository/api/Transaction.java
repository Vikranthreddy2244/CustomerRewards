package customer.rewards.repository.api;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder(toBuilder = true)
public class Transaction {
    private final String id;
    private final String customerId;
    private final Double amount;
    private final OffsetDateTime lastUpdated;
}
