package com.example.nonrelationaldatabase.service;

import com.example.nonrelationaldatabase.pojo.*;

import java.util.List;
import java.util.Map;

public interface ScenicSpotService {

    /**
     * 1. 统计概览
     * 展示景区评分的平均分、中位数、最高分、最低分、众数
     * @return 包含各项统计数据的 Map
     */
    Map<String, Object> getNums();

    /**
     * 2. 评分与星级关系
     * 展示不同星级(5A, 4A...)的平均评分
     */
    List<Map> getScoreByStarLevel();

    /**
     * 3. 城市5A景区数量 (用于GDP关联分析)
     * 获取各城市 5A 级景区的数量排名
     */
    List<CitySpotCount> get5ASpotCounts();

    /**
     * 4. (自选) 评论打分分布
     */
    List<ScoreFrequency> getReviewDistribution();

    /**
     * 4. (自选) 热门景区排行
     */
    List<Map> getTopHeatSpots();

    /**
     * 辅助：获取所有城市列表
     */
    List<String> getCities();
}