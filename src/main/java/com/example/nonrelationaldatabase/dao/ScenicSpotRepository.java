package com.example.nonrelationaldatabase.dao;

import com.example.nonrelationaldatabase.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 景区数据访问仓库
 * 对应集合: scenic_spot
 */
@Repository
public class ScenicSpotRepository {

    private final MongoTemplate mongoTemplate;

    // 集合名称常量，请确保与数据库实际集合名称一致
    private static final String COLLECTION_NAME = "scenic_spots";

    @Autowired
    public ScenicSpotRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 1. 获取景区评分(rating_score)的最大值、最小值和平均值
     * 对应 PDF 要求: 展示景区评分的平均分、中位数、最高分、最低分、众数
     * 返回对象: ScenicScoreStats (原 MaxMinAvg)
     */
    public ScenicScoreStats getScenicScoreStats() {
        Aggregation aggregation = Aggregation.newAggregation(
                // 过滤掉没有评分的数据
                Aggregation.match(Criteria.where("base_info.rating_score").ne(null)),
                Aggregation.group()
                        .max("base_info.rating_score").as("maxScore")
                        .min("base_info.rating_score").as("minScore")
                        .avg("base_info.rating_score").as("avgScore"),
                Aggregation.project("maxScore", "minScore", "avgScore").andExclude("_id")
        );
        AggregationResults<ScenicScoreStats> results =
                mongoTemplate.aggregate(aggregation, COLLECTION_NAME, ScenicScoreStats.class);

        return results.getUniqueMappedResult();
    }

    /**
     * 1. 获取景区评分的频次（用于计算众数）
     * 返回对象: ScoreFrequency (原 Mode)
     */
    public List<ScoreFrequency> getScoreFrequency() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("base_info.rating_score").ne(null)),
                // 按分数分组并计数
                Aggregation.group("base_info.rating_score").count().as("count"),
                // 将分组ID(_id)映射为 POJO 中的 score 字段
                Aggregation.project("count").and("_id").as("score"),
                // 按数量降序排列，第一个即为众数
                Aggregation.sort(Sort.Direction.DESC, "count")
        );
        AggregationResults<ScoreFrequency> results =
                mongoTemplate.aggregate(aggregation, COLLECTION_NAME, ScoreFrequency.class);
        return results.getMappedResults();
    }

    /**
     * 1. 获取所有景区评分列表（用于在 Service 层计算中位数）
     * 返回对象: ScoreResult (原 Scores)
     */
    public List<ScoreResult> getAllScores() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("base_info.rating_score").ne(null)),
                // 投影出分数，映射为 score 字段
                Aggregation.project().and("base_info.rating_score").as("score").andExclude("_id"),
                Aggregation.sort(Sort.Direction.ASC, "base_info.rating_score")
        );
        AggregationResults<ScoreResult> results =
                mongoTemplate.aggregate(aggregation, COLLECTION_NAME, ScoreResult.class);
        return results.getMappedResults();
    }

    /**
     * 2. 展示景区评分与景区星级的关系
     * 统计每个星级(5A, 4A等)的平均评分
     * 返回: List<Map> (包含 starLevel, avgScore, count)
     */
    public List<Map> getAvgScoreByStarLevel() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("base_info.star_level").ne(null)),
                Aggregation.group("base_info.star_level")
                        .avg("base_info.rating_score").as("avgScore")
                        .count().as("count"),
                Aggregation.project("avgScore", "count").and("_id").as("starLevel"),
                Aggregation.sort(Sort.Direction.DESC, "avgScore")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, Map.class);
        return results.getMappedResults();
    }

    /**
     * 3. 统计各城市 5A 级景区的数量
     * 对应 PDF 要求: "展示城市五星级景区数量与该城市GDP之间的关系"
     * 返回对象: CitySpotCount (原 CityHotelCount)
     */
    public List<CitySpotCount> get5AScenicSpotsCountByCity() {
        Aggregation aggregation = Aggregation.newAggregation(
                // 筛选 5A 景区
                Aggregation.match(Criteria.where("base_info.star_level").is("5A")),
                // 按城市分组
                Aggregation.group("base_info.city").count().as("count"),
                // 映射字段：_id -> city, count -> count
                Aggregation.project("count").and("_id").as("city"),
                Aggregation.sort(Sort.Direction.DESC, "count"),
                Aggregation.limit(20) // 取前20个热门城市
        );

        AggregationResults<CitySpotCount> results =
                mongoTemplate.aggregate(aggregation, COLLECTION_NAME, CitySpotCount.class);

        return results.getMappedResults();
    }

    /**
     * 4. (自选题目) 统计所有评论中，用户打分(user_rating)的分布情况
     * 复用 ScoreFrequency 类来存储 "星级-数量"
     */
    public List<ScoreFrequency> getReviewRatingDistribution() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("reviews"), // 展开 reviews 数组
                Aggregation.match(Criteria.where("reviews.user_rating").ne(null)),
                Aggregation.group("reviews.user_rating").count().as("count"),
                // 复用 ScoreFrequency: score即星级(1-5), count即数量
                Aggregation.project("count").and("_id").as("score"),
                Aggregation.sort(Sort.Direction.ASC, "score")
        );

        AggregationResults<ScoreFrequency> results =
                mongoTemplate.aggregate(aggregation, COLLECTION_NAME, ScoreFrequency.class);
        return results.getMappedResults();
    }

    /**
     * 4. (自选题目) 获取热度分(heat_score)最高的10个景区名称和热度
     * 返回: List<Map> (包含 name, heat_score)
     */
    public List<Map> getTopHeatScenicSpots() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "base_info.heat_score"));
        query.limit(10);
        query.fields().include("base_info.name").include("base_info.heat_score");

        return mongoTemplate.find(query, Map.class, COLLECTION_NAME);
    }

    /**
     * 辅助方法：获取所有城市列表
     */
    public List<String> getCities() {
        Query query = new Query();
        return mongoTemplate.findDistinct(query, "base_info.city", COLLECTION_NAME, String.class);
    }
}