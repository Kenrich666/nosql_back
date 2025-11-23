package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("hotel")
public class Hotel {
    @Field("hotel_name")
    private String name;
    @Field("hotel_score")
    private Double score;
    @Field("hotel_image_id")
    private String imageUrl;
    @Field("hotel_location_info")
    private String location;
    @Field("hotel_grade_text")
    private String grade;
    @Field("hotel_comment_desc")
    private String comment;
    @Field("hotel_city_name")
    private String city;
    @Field("latitude")
    private Coordinate coordinate;
    @Field("rooms")
    private Room[] rooms;

}
