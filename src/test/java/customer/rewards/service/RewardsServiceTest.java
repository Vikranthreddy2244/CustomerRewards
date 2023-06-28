package customer.rewards.service;

import customer.rewards.exceptions.CustomerNotFoundException;
import customer.rewards.model.MonthlyReward;
import customer.rewards.model.Rewards;
import customer.rewards.repository.RewardsRepository;
import customer.rewards.repository.api.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RewardsServiceTest {

    @InjectMocks
    private RewardsService rewardsService;

    @Mock
    private RewardsRepository rewardsRepository;

    public List<Transaction> getMockTransactions(){
        return List.of(
                buildTransaction(1L, 1L, 120D, "2023-04-12T06:30:15-06:00"),
                buildTransaction(2L, 1L, 85D, "2023-04-01T06:30:15-06:00"),
                buildTransaction(3L, 1L, 160D, "2023-03-04T06:30:15+07:00"),
                buildTransaction(4L, 1L, 90D, "2023-03-01T06:30:15+07:00"),
                buildTransaction(5L, 1L, 120D, "2023-02-04T06:30:15+07:00"),
                buildTransaction(6L, 1L, 165D, "2023-02-05T06:30:15+07:00"),
                buildTransaction(7L, 2L, 113D, "2023-04-05T06:30:15+07:00"),
                buildTransaction(8L, 2L, 80D, "2023-03-27T06:30:15+07:00"),
                buildTransaction(9L, 2L, 102D, "2023-03-04T06:30:15+07:00"),
                buildTransaction(10L, 2L, 210D, "2023-03-01T06:30:15+07:00"),
                buildTransaction(11L, 2L, 130D, "2023-02-27T06:30:15+07:00"),
                buildTransaction(12L, 2L, 88D, "2023-04-15T06:30:15+07:00"),
                buildTransaction(13L, 3L, 102D, "2023-04-05T06:30:15+07:00"),
                buildTransaction(14L, 3L, 84D, "2023-03-27T06:30:15+07:00"),
                buildTransaction(15L, 3L, 200D, "2023-03-04T06:30:15+07:00"),
                buildTransaction(16L, 3L, 103D, "2023-03-01T06:30:15+07:00"),
                buildTransaction(17L, 3L, 500D, "2023-03-27T06:30:15+07:00"),
                buildTransaction(18L, 3L, 105D, "2023-04-20T06:30:15+07:00"),
                buildTransaction(19L, 3L, 102D, "2023-03-14T06:30:15+07:00")
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

    @Test
    public void testRetrieveRewardPoints() {
        // Mocking the repository to return a list of transactions
        List<Transaction> transactions = getMockTransactions();
        when(rewardsRepository.getTransactions()).thenReturn(transactions);

        // Calling the method under test
        Rewards result = rewardsService.retrieveRewardPoints("1", 3);

        // Assertions
        assertEquals("1", result.getCustomerId());
        assertEquals(605, result.getPoints());

        List<MonthlyReward> monthlyRewards = result.getMonthlyRewards();
        assertEquals(3, monthlyRewards.size());

        // Assuming the list is sorted in descending order of month
        assertEquals("APRIL", monthlyRewards.get(0).getMonth());
        assertEquals(125, monthlyRewards.get(0).getPoints());

        assertEquals("MARCH", monthlyRewards.get(1).getMonth());
        assertEquals(210, monthlyRewards.get(1).getPoints());

        assertEquals("FEBRUARY", monthlyRewards.get(2).getMonth());
        assertEquals(270, monthlyRewards.get(2).getPoints());
    }

    @Test
    public void testRetrieveRewardPoints_CustomerNotFound() {
        // Mocking the repository to return an empty list of transactions
        when(rewardsRepository.getTransactions()).thenReturn(Collections.emptyList());

        // Calling the method under test and asserting the exception
        assertThrows(CustomerNotFoundException.class, () -> {
            rewardsService.retrieveRewardPoints("789", 6);
        });

        // Verifying that the repository method was called
        verify(rewardsRepository, times(1)).getTransactions();
    }

    @Test
    public void testRetrieveAllRewardPoints() {
        // Mocking the repository to return a list of transactions for multiple customers
        List<Transaction> transactions = getMockTransactions();
        when(rewardsRepository.getTransactions()).thenReturn(transactions);

        // Calling the method under test
        List<Rewards> result = rewardsService.retrieveAllRewardPoints(2);

        // Assertions
        assertEquals(3, result.size());

        Rewards rewards1 = result.get(0);
        assertEquals("1", rewards1.getCustomerId());
        assertEquals(605, rewards1.getPoints());

        List<MonthlyReward> monthlyRewards1 = rewards1.getMonthlyRewards();
        assertEquals(2, monthlyRewards1.size());

        // Assuming the list is sorted in descending order of month
        assertEquals("APRIL", monthlyRewards1.get(0).getMonth());
        assertEquals(125, monthlyRewards1.get(0).getPoints());

        assertEquals("MARCH", monthlyRewards1.get(1).getMonth());
        assertEquals(210, monthlyRewards1.get(1).getPoints());

        Rewards rewards2 = result.get(1);
        assertEquals("2", rewards2.getCustomerId());
        assertEquals(578, rewards2.getPoints());
    }
}