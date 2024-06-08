package com.capgemini.wsb.fitnesstracker.statistics.api;

import java.util.List;

public interface StatisticsService {
    List<Statistics> getAllStatistics();
    Statistics getStatisticsById(Long id);
    Statistics createStatistics(Statistics statistics);
    Statistics updateStatistics(Long id, Statistics statistics);
    void deleteStatistics(Long id);
}
