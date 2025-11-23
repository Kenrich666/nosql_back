package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 评分频次实体类
 * 用于众数计算和评分分布统计
 */
@Data
public class ScoreFrequency {
    // 对应聚合查询中的 _id (即具体的评分值)
    @Field("score")
    private Double score;

    // 对应聚合查询中的 count
    private Integer count;
}