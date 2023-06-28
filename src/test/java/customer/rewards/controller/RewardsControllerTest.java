package customer.rewards.controller;

import customer.rewards.model.MonthlyReward;
import customer.rewards.model.Rewards;
import customer.rewards.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RewardsController.class)
class RewardsControllerTest {

    @MockBean
    private RewardsService rewardsService;

    @InjectMocks
    private RewardsController rewardsController;

    @Autowired
    private MockMvc mockMvc;

    private Rewards getRewards() {
        MonthlyReward monthlyReward = MonthlyReward.builder()
                .month("APRIL")
                .points(125)
                .build();

        Rewards rewards = Rewards.builder()
                .customerId("1")
                .points(605)
                .monthlyRewards(List.of(monthlyReward))
                .build();
        return rewards;
    }

    @Test
    void getRewardsForCustomer_withCustomerId() throws Exception {
        String customerId = "1";
        int defaultNumberOfMonths = 3;

        Rewards rewards = getRewards();

        when(rewardsService.retrieveRewardPoints(eq(customerId), eq(defaultNumberOfMonths))).thenReturn(rewards);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}/rewards", customerId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.points").exists())
                .andReturn();
    }

    @Test
    void getRewardsForAllCustomers() throws Exception {
        int defaultNumberOfMonths = 3;
        List<Rewards> rewardsList = Arrays.asList(getRewards(), getRewards());

        when(rewardsService.retrieveAllRewardPoints(eq(defaultNumberOfMonths))).thenReturn(rewardsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/rewards")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].points").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].points").exists())
                .andReturn();
    }
}
