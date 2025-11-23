package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 景区基本信息实体类
 */
@Data
public class BaseInfo {

    @Field("name")
    private String name;

    @Field("city")
    private String city;

    @Field("star_level")
    private String starLevel;

    @Field("heat_score")
    private Double heatScore;

    @Field("rating_score")
    private Double ratingScore;

    @Field("address")
    private String address;

    @Field("phone")
    private String phone;

    @Field("opening_hours")
    private String openingHours;

    @Field("signal_status")
    private String signalStatus;

    @Field("total_comments")
    private Integer totalComments;
}