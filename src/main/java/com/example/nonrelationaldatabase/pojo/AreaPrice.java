package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class AreaPrice {
    @Field("_id")
    private String interval;
    @Field("room_prices")
    private List<Integer> prices;
    @Field("count")
    private Integer count;
}
