package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public List<Statistics> getAllStatistics() {
        return statisticsService.getAllStatistics();
    }

    @GetMapping("/{id}")
    public Statistics getStatisticsById(@PathVariable Long id) {
        return statisticsService.getStatisticsById(id);
    }

    @PostMapping
    public Statistics createStatistics(@RequestBody Statistics statistics) {
        return statisticsService.createStatistics(statistics);
    }

    @PutMapping("/{id}")
    public Statistics updateStatistics(@PathVariable Long id, @RequestBody Statistics statistics) {
        return statisticsService.updateStatistics(id, statistics);
    }

    @DeleteMapping("/{id}")
    public void deleteStatistics(@PathVariable Long id) {
        statisticsService.deleteStatistics(id);
    }
}
