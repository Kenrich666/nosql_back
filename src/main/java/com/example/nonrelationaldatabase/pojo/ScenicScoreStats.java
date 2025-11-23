package com.example.nonrelationaldatabase.pojo;

import lombok.Data;

/**
 * 景区评分统计实体类
 * 对应聚合查询中的 maxScore, minScore, avgScore
 */
@Data
public class ScenicScoreStats {
    private Double maxScore;
    private Double minScore;
    private Double avgScore;
}