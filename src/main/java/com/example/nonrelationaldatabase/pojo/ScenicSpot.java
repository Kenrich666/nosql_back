package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * 景区主实体类
 * 对应集合名称建议为 "scenic_spot" 或根据实际导入情况调整
 */
@Data
@Document("scenic_spots")
public class ScenicSpot {

    // 对应 JSON 中的 base_info 对象
    @Field("base_info")
    private BaseInfo baseInfo;

    // 对应 JSON 中的 surroundings 对象
    @Field("surroundings")
    private Surroundings surroundings;

    // 对应 JSON 中的 reviews 数组
    @Field("reviews")
    private List<Review> reviews;
}