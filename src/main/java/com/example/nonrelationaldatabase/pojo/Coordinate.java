package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Coordinate {

    @Field("latitude")
    private Double lat;
    @Field("longitude")
    private Double lng;
}
