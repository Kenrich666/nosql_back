package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class LocationInfo {
    @Field("hotel_name")
    private String name;
    @Field("hotel_location_info")
    private String location;
}
