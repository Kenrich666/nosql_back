package com.example.nonrelationaldatabase.controller;

import com.example.nonrelationaldatabase.pojo.CitySpotCount;
import com.example.nonrelationaldatabase.pojo.ScoreFrequency;
import com.example.nonrelationaldatabase.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 景区数据控制器
 * 处理前端关于景区统计分析的请求
 */
@RestController
@RequestMapping("/scenic")
public class ScenicSpotController {

    private final ScenicSpotService scenicSpotService;

    @Autowired
    public ScenicSpotController(ScenicSpotService scenicSpotService) {
        this.scenicSpotService = scenicSpotService;
    }

    /**
     * 1. 获取景区评分的统计概览
     * 包括：最大值、最小值、平均值、众数、中位数
     * 对应 PDF 实验要求第 1 点
     * 访问地址: /scenic/nums
     */
    @RequestMapping("/nums")
    public Map<String, Object> getNums() {
        return scenicSpotService.getNums();
    }

    /**
     * 2. 获取不同星级景区的平均评分
     * 展示景区评分与景区星级(5A, 4A...)的关系
     * 对应 PDF 实验要求第 2 点
     * 访问地址: /scenic/starLevelScore
     */
    @RequestMapping("/starLevelScore")
    public List<Map> getScoreByStarLevel() {
        return scenicSpotService.getScoreByStarLevel();
    }

    /**
     * 3. 获取各城市 5A 级景区的数量排名
     * 前端可结合 GDP 数据进行散点图或关联分析展示
     * 对应 PDF 实验要求第 3 点
     * 访问地址: /scenic/city5ACount
     */
    @RequestMapping("/city5ACount")
    public List<CitySpotCount> get5ASpotCounts() {
        return scenicSpotService.get5ASpotCounts();
    }

    /**
     * 4. (自选题目) 获取评论打分的分布情况
     * 用于展示游客满意度直方图
     * 访问地址: /scenic/reviewDistribution
     */
    @RequestMapping("/reviewDistribution")
    public List<ScoreFrequency> getReviewDistribution() {
        return scenicSpotService.getReviewDistribution();
    }

    /**
     * 4. (自选题目) 获取热度最高的 10 个景区
     * 用于展示热门榜单
     * 访问地址: /scenic/topHeat
     */
    @RequestMapping("/topHeat")
    public List<Map> getTopHeatSpots() {
        return scenicSpotService.getTopHeatSpots();
    }

    /**
     * 辅助接口：获取所有城市列表
     * 可能用于前端下拉筛选
     * 访问地址: /scenic/cities
     */
    @RequestMapping("/cities")
    public List<String> getCities() {
        return scenicSpotService.getCities();
    }
}