package customer.rewards.service;

import customer.rewards.exceptions.CustomerNotFoundException;
import customer.rewards.model.MonthlyReward;
import customer.rewards.model.Rewards;
import customer.rewards.repository.RewardsRepository;
import customer.rewards.repository.api.Transaction;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardsService {

    private final RewardsRepository rewardsRepository;
    //CustomerId, <Month, Reward-Point>
    Map<String, Map<Month, Integer>> pointsForCustomerMonthly = new HashMap<>();
    //CustomerId, Reward-Point
    Map<String, Integer> totalPointsForCustomer = new HashMap<>();

    public Rewards retrieveRewardPoints(String reqCustomerId, Integer numberOfMonths){
        calculateRewardsForTransactions();
        return calculateRewardPoints(reqCustomerId, numberOfMonths);
    }

    public List<Rewards> retrieveAllRewardPoints(Integer numberOfMonths){
        calculateRewardsForTransactions();
        return calculateAllRewardPoints(numberOfMonths);
    }

    private void calculateRewardsForTransactions() {
        pointsForCustomerMonthly = new HashMap<>();
        totalPointsForCustomer = new HashMap<>();
        List<Transaction> transactions = this.rewardsRepository.getTransactions();
        for (Transaction transaction : transactions) {
            String customerId = transaction.getCustomerId();
            Month month = transaction.getLastUpdated().getMonth();

            int points = calculatePoints(transaction.getAmount());
            Comparator<Month> reverseMonthComparator = Comparator.comparingInt(Month::getValue).reversed();
            pointsForCustomerMonthly.putIfAbsent(customerId, new TreeMap<>(reverseMonthComparator));
            Map<Month, Integer> customerPoints = pointsForCustomerMonthly.get(customerId);
            customerPoints.put(month, customerPoints.getOrDefault(month, 0) + points);

            totalPointsForCustomer.put(customerId, totalPointsForCustomer.getOrDefault(customerId, 0) + points);
        }
    }

    public Rewards calculateRewardPoints(String reqCustomerId, Integer numberOfMonths) {

        List<MonthlyReward> monthlyRewards = new ArrayList<>();

        Map<Month, Integer> monthlyPoints = pointsForCustomerMonthly.get(reqCustomerId);

        if (monthlyPoints != null) {
            extractMonthlyRewards(numberOfMonths, monthlyPoints, monthlyRewards);
        } else {
            log.info("No points found for customer: " + reqCustomerId);
            throw new CustomerNotFoundException("Customer not found: " + reqCustomerId);
        }

        return Rewards.builder()
                .customerId(reqCustomerId)
                .points(totalPointsForCustomer.get(reqCustomerId))
                .monthlyRewards(monthlyRewards)
                .build();
    }

    public List<Rewards> calculateAllRewardPoints(Integer numberOfMonths) {
        List<Rewards> rewardsList = new ArrayList<>();
        for (String customerId : pointsForCustomerMonthly.keySet()) {
            List<MonthlyReward> monthlyRewards = new ArrayList<>();
            Map<Month, Integer> monthPoints = pointsForCustomerMonthly.get(customerId);
            extractMonthlyRewards(numberOfMonths, monthPoints, monthlyRewards);
            rewardsList.add(Rewards.builder()
                    .customerId(customerId)
                    .points(totalPointsForCustomer.get(customerId))
                    .monthlyRewards(monthlyRewards)
                    .build());
        }
        return rewardsList;
    }

    private void extractMonthlyRewards(Integer numberOfMonths, Map<Month, Integer> monthlyPoints, List<MonthlyReward> monthlyRewards) {
        int count = 0;
        for (Map.Entry<Month, Integer> monthPoints : monthlyPoints.entrySet()) {
            Month month = monthPoints.getKey();
            Integer points = monthPoints.getValue();
            monthlyRewards.add(MonthlyReward.builder().month(month.toString()).points(points).build());
            count = count + 1;
            if(count == numberOfMonths){
                break;
            }
        }
    }

    private int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int) ((amount - 100) * 2);
        }
        if (amount > 50) {
            points += (int) (Math.min(amount, 100) - 50);
        }
        return points;
    }
}
