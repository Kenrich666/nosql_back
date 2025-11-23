package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * 周边及推荐信息实体类
 */
@Data
public class Surroundings {

    @Field("nearby_attractions")
    private List<String> nearbyAttractions;

    @Field("nearby_food")
    private List<String> nearbyFood;

    @Field("nearby_shops")
    private List<String> nearbyShops;

    @Field("recommended_routes")
    private List<String> recommendedRoutes;

    @Field("recommended_food")
    private List<String> recommendedFood;
}