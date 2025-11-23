package com.example.nonrelationaldatabase.dao;


import com.example.nonrelationaldatabase.pojo.*;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒店数据访问仓库
 */
@Repository
public class HotelRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public HotelRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 获取酒店评分的最大值、最小值和平均值
     * @return MaxMinAvg 包含最大、最小、平均分的对象
     */
    public MaxMinAvg getMaxMinAvg() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("hotel_score").ne(Double.NaN)),
                Aggregation.group().max("hotel_score").as("maxScore")
                        .min("hotel_score").as("minScore")
                        .avg("hotel_score").as("avgScore"),
                Aggregation.project("maxScore", "minScore", "avgScore").andExclude("_id")

        );
        AggregationResults<MaxMinAvg> results =
                mongoTemplate.aggregate(aggregation, "information", MaxMinAvg.class);

        return results.getUniqueMappedResult();
    }

    /**
     * 获取酒店评分的众数
     * @return Mode 列表，包含分数和出现次数
     */
    public List<Mode> getMode() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("hotel_score").ne(Double.NaN)),
                Aggregation.group("hotel_score").count().as("count"),
                Aggregation.project("count").and("_id").as("score"),
                Aggregation.sort(Sort.Direction.DESC, "count")
        );
        AggregationResults<Mode> results =
                mongoTemplate.aggregate(aggregation, "information", Mode.class);
        return results.getMappedResults();
    }

    /**
     * 获取所有酒店评分用于计算中位数
     * @return Scores 列表，包含所有酒店的分数
     */
    public List<Scores> getMedian() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("hotel_score").ne(Double.NaN)),
                Aggregation.project().and("hotel_score").as("score").andExclude("_id"),
                Aggregation.sort(Sort.Direction.ASC, "hotel_score")
        );
        AggregationResults<Scores> results =
                mongoTemplate.aggregate(aggregation, "information", Scores.class);
        return results.getMappedResults();
    }

    /**
     * 获取所有不同的酒店等级
     * @return 字符串列表，包含所有不同的酒店等级文本
     */
    public List<String> getDiffGrades() {
        Query query = new Query();
        return mongoTemplate.findDistinct(query, "hotel_grade_text", "information", String.class);
    }

    /**
     * 根据酒店等级获取酒店列表，包含酒店名称、价格、城市和分数
     * @param hotelGrade 酒店等级
     * @return ScorePrice 列表
     */
    public List<ScorePrice> getHotels(String hotelGrade) {
        Criteria criteria =
                hotelGrade != null ? Criteria.where("hotel_grade_text").is(hotelGrade) : new Criteria();

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria.and("hotel_score").ne(Double.NaN)),
                Aggregation.unwind("rooms"),
                Aggregation.group("hotel_name", "hotel_score", "hotel_city_name")
                        .max("rooms.room_price").as("price"),
                Aggregation.group("hotel_score")
                        .push(new BasicDBObject("name", "$_id.hotel_name")
                                .append("price", "$price")
                                .append("city", "$_id.hotel_city_name")).as("hotels"),
                Aggregation.project().and("_id").as("score")
                        .and("hotels").as("hotels")
                        .andExclude("_id"),
                Aggregation.sort(Sort.Direction.ASC, "score")
        );
//        System.out.println(hotelGrade);

        AggregationResults<ScorePrice> results =
                mongoTemplate.aggregate(aggregation, "information", ScorePrice.class);
        return results.getMappedResults();
    }

    /**
     * 根据城市获取酒店的位置信息
     * @param city 城市名称
     * @return LocationInfo 列表
     */
    public List<LocationInfo> getLocations(String city) {
        Query query = new Query();
        query.addCriteria(Criteria.where("hotel_city_name").is(city));
        query.fields().include("hotel_name", "hotel_location_info").exclude("_id");

        return mongoTemplate.find(query, LocationInfo.class, "information");
    }

    /**
     * 更新酒店的坐标
     * @param hotelName 酒店名称
     * @param latitude 纬度
     * @param longitude 经度
     */
    public void updateCoordinate(String hotelName, Double latitude, Double longitude) {
        // 创建查询条件，查询 hotel_name 匹配的文档
        Query query = new Query();
        query.addCriteria(Criteria.where("hotel_name").is(hotelName));

        // 创建更新对象，设置 coordinate
        Update update = new Update();
        update.set("coordinate.latitude", latitude);
        update.set("coordinate.longitude", longitude);

        // 执行更新操作
        mongoTemplate.updateFirst(query, update, "information"); // 更新第一个匹配的文档
    }

    /**
     * 根据城市获取酒店的坐标
     * @param city 城市名称
     * @return Coordinate 列表
     */
    public List<Coordinate> getCoordinate(String city) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("hotel_city_name").is(city)),
                Aggregation.project().and("coordinate.latitude").as("latitude")
                        .and("coordinate.longitude").as("longitude")
                        .andExclude("_id")
        );
        AggregationResults<Coordinate> result = mongoTemplate.aggregate(aggregation, "information", Coordinate.class);
        return result.getMappedResults();
    }

    /**
     * 获取所有城市列表
     * @return 字符串列表，包含所有城市名称
     */
    public List<String> getCities() {
        Query query = new Query();
        return mongoTemplate.findDistinct(query, "hotel_city_name", "information", String.class);
    }

    /**
     * 根据酒店等级统计各城市的酒店数量
     * @param grade 酒店等级
     * @return CityHotelCount 列表
     */
    public List<CityHotelCount> countHotelsByCity(String grade) {
        Criteria criteria = grade != null ? Criteria.where("hotel_grade_text").is(grade) : new Criteria();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("hotel_city_name")
                        .count().as("hotelCount"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "hotelCount"))
        );

        AggregationResults<CityHotelCount> results =
                mongoTemplate.aggregate(aggregation, "information", CityHotelCount.class);

        return results.getMappedResults();
    }

    /**
     * 统计各城市的房间总数
     * @return CityRoomCount 列表
     */
    public List<CityRoomCount> countRoomsByCity() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("rooms"),               // 展开 rooms 数组
                Aggregation.group("hotel_city_name")       // 按城市分组
                        .count().as("roomCount"),             // 统计房间数量
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "roomCount")) // 按房间数量降序排序
        );

        // 执行聚合查询
        AggregationResults<CityRoomCount> results =
                mongoTemplate.aggregate(aggregation, "information", CityRoomCount.class);

        return results.getMappedResults();
    }

    /**
     * 分析酒店评论，按分数段统计评论词频
     * @return CommentFrequency 列表
     */
    public List<CommentFrequency> analyzeHotelComments() {
        BucketOperation bucketOperation = Aggregation.bucket("hotel_score")
                .withBoundaries(4.0, 4.4, 4.8, 5.0)
                .withDefaultBucket("other")
                .andOutput("hotel_comment_desc").push().as("comments")
                .andOutput("hotel_score").count().as("count");
        UnwindOperation unwindOperation = Aggregation.unwind("comments");
        GroupOperation groupByComment = Aggregation.group("_id", "comments")
                .count().as("count");
        GroupOperation groupByRange = Aggregation.group("_id._id")
                .push(new BasicDBObject("comment", "$_id.comments")
                        .append("count", "$count")).as("comments");

        Aggregation aggregation = Aggregation.newAggregation(bucketOperation, unwindOperation,
                groupByComment, groupByRange, Aggregation.sort(Sort.by("_id")));

        AggregationResults<CommentFrequency> results =
                mongoTemplate.aggregate(aggregation, "information", CommentFrequency.class);
        return results.getMappedResults();
    }

    /**
     * 按房间面积区间分析房间价格
     * @return AreaPrice 列表
     */
    public List<AreaPrice> getRoomPriceByArea() {
        BucketOperation bucketOperation = Aggregation.bucket("rooms.room_area")
                .withBoundaries(0, 10, 20, 40, 100, 150, 200, Double.POSITIVE_INFINITY)
                .withDefaultBucket("other")
                .andOutput("rooms.room_price").push().as("room_prices")
                .andOutput("rooms.room_area").count().as("count");
        UnwindOperation unwindOperation = Aggregation.unwind("rooms");
        MatchOperation matchOperation = Aggregation.match(Criteria.where("rooms.room_area").gt(0));

        Aggregation aggregation = Aggregation.newAggregation(unwindOperation, matchOperation,
                bucketOperation, Aggregation.sort(Sort.by("_id")));

        AggregationResults<AreaPrice> results =
                mongoTemplate.aggregate(aggregation, "information", AreaPrice.class);
        return results.getMappedResults();
    }


}
