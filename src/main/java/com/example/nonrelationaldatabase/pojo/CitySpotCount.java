package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 城市景区数量统计实体类
 */
@Data
public class CitySpotCount {
    // 对应聚合查询中的 _id (即城市名称)
    @Field("_id")
    private String city;

    // 对应聚合查询中的 count
    @Field("count")
    private Integer count;
}