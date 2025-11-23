package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Room {
    @Field("room_name")
    private String type;
    @Field("room_image_url")
    private String imageUrl;
    @Field("room_area")
    private String area;
    @Field("room_bed_type")
    private String bedType;
    @Field("room_window")
    private String window;
    @Field("room_breakfast_num")
    private String breakfastNum;
    @Field("room_wifi")
    private String network;
    @Field("room_price")
    private Integer price;
    @Field("room_exist_num")
    private String guestNum;

}
