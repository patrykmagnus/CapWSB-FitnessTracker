package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAll();
    }

    @Override
    public Statistics getStatisticsById(Long id) {
        return statisticsRepository.findById(id).orElse(null);
    }

    @Override
    public Statistics createStatistics(Statistics statistics) {
        return statisticsRepository.save(statistics);
    }

    @Override
    public Statistics updateStatistics(Long id, Statistics statistics) {
        Statistics existingStatistics = statisticsRepository.findById(id).orElse(null);
        if (existingStatistics != null) {
            existingStatistics.setTotalTrainings(statistics.getTotalTrainings());
            existingStatistics.setTotalDistance(statistics.getTotalDistance());
            existingStatistics.setTotalCaloriesBurned(statistics.getTotalCaloriesBurned());
            existingStatistics.setUser(statistics.getUser());
            return statisticsRepository.save(existingStatistics);
        }
        return null;
    }

    @Override
    public void deleteStatistics(Long id) {
        statisticsRepository.deleteById(id);
    }
}
