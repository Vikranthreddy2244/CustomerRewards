package customer.rewards.controller;

import customer.rewards.model.Rewards;
import customer.rewards.service.RewardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class RewardsController {

    private final RewardsService rewardsService;

    @GetMapping(value = {"/{id}/rewards", "/{id}/rewards/{months}"})
    public Rewards getRewardsForCustomer(@PathVariable("id") String customerId, @PathVariable("months") Optional<Integer> numberOfMonths){
        return this.rewardsService.retrieveRewardPoints(customerId, getNumOfMonths(numberOfMonths));
    }

    @GetMapping(value = {"/rewards", "/rewards/{months}"})
    public List<Rewards> getRewardsForCustomer(@PathVariable("months") Optional<Integer> numberOfMonths){
        return this.rewardsService.retrieveAllRewardPoints(getNumOfMonths(numberOfMonths));
    }

    private int getNumOfMonths(@PathVariable("months") Optional<Integer> numberOfMonths) {
        int numOfMonths = 3;
        if (numberOfMonths.isPresent()) {
            numOfMonths = numberOfMonths.get();
        }
        return numOfMonths;
    }
}
