package com.example.nonrelationaldatabase.pojo;

import lombok.Data;

/**
 * 单项评分包装类
 * 用于获取所有评分列表以计算中位数
 */
@Data
public class ScoreResult {
    private Double score;
}