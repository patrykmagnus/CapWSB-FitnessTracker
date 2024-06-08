package com.capgemini.wsb.fitnesstracker.statistics;

import com.capgemini.wsb.fitnesstracker.IntegrationTest;
import com.capgemini.wsb.fitnesstracker.IntegrationTestBase;
import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.internal.StatisticsRepository;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.LocalDate.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class StatisticsApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    private User user1;
    private Statistics statistics1;
    private Statistics statistics2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(generateUser());
        statistics1 = statisticsRepository.save(generateStatistics(user1));
        statistics2 = statisticsRepository.save(generateStatistics(user1));
    }

    @Test
    void shouldReturnAllStatistics_whenGettingAllStatistics() throws Exception {
        mockMvc.perform(get("/api/statistics").contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].user.id").value(user1.getId()))
                .andExpect(jsonPath("$[0].totalTrainings").value(statistics1.getTotalTrainings()))
                .andExpect(jsonPath("$[0].totalDistance").value(statistics1.getTotalDistance()))
                .andExpect(jsonPath("$[0].totalCaloriesBurned").value(statistics1.getTotalCaloriesBurned()))
                .andExpect(jsonPath("$[1].user.id").value(user1.getId()))
                .andExpect(jsonPath("$[1].totalTrainings").value(statistics2.getTotalTrainings()))
                .andExpect(jsonPath("$[1].totalDistance").value(statistics2.getTotalDistance()))
                .andExpect(jsonPath("$[1].totalCaloriesBurned").value(statistics2.getTotalCaloriesBurned()));
    }

    @Test
    void shouldPersistStatistics_whenCreatingNewStatistics() throws Exception {
        String requestBody = """
                {
                    "user": {
                        "id": "%s"
                    },
                    "totalTrainings": 10,
                    "totalDistance": 100.0,
                    "totalCaloriesBurned": 200
                }
                """.formatted(user1.getId());

        mockMvc.perform(post("/api/statistics").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(user1.getId()))
                .andExpect(jsonPath("$.totalTrainings").value(10))
                .andExpect(jsonPath("$.totalDistance").value(100.0))
                .andExpect(jsonPath("$.totalCaloriesBurned").value(200));

        List<Statistics> allStatistics = statisticsRepository.findAll();
        Statistics statistics = allStatistics.get(allStatistics.size() - 1);

        assertThat(statistics.getUser().getId()).isEqualTo(user1.getId());
        assertThat(statistics.getTotalTrainings()).isEqualTo(10);
        assertThat(statistics.getTotalDistance()).isEqualTo(100.0);
        assertThat(statistics.getTotalCaloriesBurned()).isEqualTo(200);
    }

    private User generateUser() {
        return new User(randomUUID().toString(), "John", now(), "john.doe@example.com");
    }

    private Statistics generateStatistics(User user) {
        return Statistics.createStatistics(user, 10, 100.0, 200);
    }
}
