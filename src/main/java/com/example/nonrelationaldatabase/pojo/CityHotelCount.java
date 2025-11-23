package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class CityHotelCount {
    @Field("_id")
    private String hotelCityName;
    @Field("hotelCount")
    private Integer hotelCount;
}
