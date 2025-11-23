package com.example.nonrelationaldatabase.service;

import com.example.nonrelationaldatabase.dao.ScenicSpotRepository;
import com.example.nonrelationaldatabase.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScenicSpotServiceImpl implements ScenicSpotService {

    private final ScenicSpotRepository scenicSpotRepository;

    @Autowired
    public ScenicSpotServiceImpl(ScenicSpotRepository scenicSpotRepository) {
        this.scenicSpotRepository = scenicSpotRepository;
    }

    @Override
    public Map<String, Object> getNums() {
        Map<String, Object> map = new HashMap<>();

        // 1. 获取最大、最小、平均值 (利用 MongoDB 聚合)
        ScenicScoreStats stats = scenicSpotRepository.getScenicScoreStats();

        // 2. 获取众数列表 (利用 MongoDB 聚合)
        List<ScoreFrequency> modes = scenicSpotRepository.getScoreFrequency();

        // 3. 计算中位数 (内存计算)
        // 获取所有分数并已排序
        List<ScoreResult> scoreList = scenicSpotRepository.getAllScores();
        int len = scoreList.size();
        Double median = 0.0;
        if (len > 0) {
            if (len % 2 == 0) {
                // 偶数个，取中间两个的平均值
                Double m1 = scoreList.get((len - 1) / 2).getScore();
                Double m2 = scoreList.get(len / 2).getScore();
                median = (m1 + m2) / 2.0;
            } else {
                // 奇数个，取中间值
                median = scoreList.get(len / 2).getScore();
            }
        }

        // 组装结果
        map.put("max", stats != null ? stats.getMaxScore() : 0);
        map.put("min", stats != null ? stats.getMinScore() : 0);
        map.put("avg", stats != null ? stats.getAvgScore() : 0);
        map.put("mode", modes); // 返回众数列表，前端可取第一个即为出现最多的
        map.put("median", median);

        return map;
    }

    @Override
    public List<Map> getScoreByStarLevel() {
        return scenicSpotRepository.getAvgScoreByStarLevel();
    }

    @Override
    public List<CitySpotCount> get5ASpotCounts() {
        return scenicSpotRepository.get5AScenicSpotsCountByCity();
    }

    @Override
    public List<ScoreFrequency> getReviewDistribution() {
        return scenicSpotRepository.getReviewRatingDistribution();
    }

    @Override
    public List<Map> getTopHeatSpots() {
        return scenicSpotRepository.getTopHeatScenicSpots();
    }

    @Override
    public List<String> getCities() {
        return scenicSpotRepository.getCities();
    }
}