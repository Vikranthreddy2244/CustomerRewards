package customer.rewards.repository;

import customer.rewards.repository.api.Transaction;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class RewardsRepository {
    public List<Transaction> getTransactions(){
        return List.of(
                buildTransaction(1L, 1L, 120D, "2023-04-12T06:30:15-06:00"),
                buildTransaction(2L, 1L, 85D, "2023-04-01T06:30:15-06:00"),
                buildTransaction(3L, 1L, 160D, "2023-03-04T06:30:15-06:00"),
                buildTransaction(4L, 1L, 90D, "2023-03-01T06:30:15-06:00"),
                buildTransaction(5L, 1L, 120D, "2023-02-04T06:30:15-06:00"),
                buildTransaction(6L, 1L, 165D, "2023-02-05T06:30:15-06:00"),
                buildTransaction(7L, 2L, 113D, "2023-04-05T06:30:15-06:00"),
                buildTransaction(8L, 2L, 80D, "2023-03-27T06:30:15-06:00"),
                buildTransaction(9L, 2L, 102D, "2023-03-04T06:30:15-06:00"),
                buildTransaction(10L, 2L, 210D, "2023-03-01T06:30:15-06:00"),
                buildTransaction(11L, 2L, 130D, "2023-02-27T06:30:15-06:00"),
                buildTransaction(12L, 2L, 88D, "2023-04-15T06:30:15-06:00"),
                buildTransaction(13L, 3L, 102D, "2023-04-05T06:30:15-06:00"),
                buildTransaction(14L, 3L, 84D, "2023-03-27T06:30:15-06:00"),
                buildTransaction(15L, 3L, 200D, "2023-03-04T06:30:15-06:00"),
                buildTransaction(16L, 3L, 103D, "2023-03-01T06:30:15-06:00"),
                buildTransaction(17L, 3L, 500D, "2023-03-27T06:30:15-06:00"),
                buildTransaction(18L, 3L, 105D, "2023-04-20T06:30:15-06:00"),
                buildTransaction(19L, 3L, 102D, "2023-03-14T06:30:15-06:00")
        );
    }

    private Transaction buildTransaction(Long id, Long customerId, Double amount, String dateTime) {
        return Transaction.builder()
                .id(id.toString())
                .customerId(customerId.toString())
                .amount(amount)
                .lastUpdated(OffsetDateTime.parse(dateTime))
                .build();
    }
}
