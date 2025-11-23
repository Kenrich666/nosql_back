package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class CityRoomCount {
    @Field("_id")
    private String hotelCityName;
    @Field("roomCount")
    private Integer roomCount;
}
