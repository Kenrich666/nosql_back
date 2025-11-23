package com.example.nonrelationaldatabase.service;

import com.example.nonrelationaldatabase.pojo.*;

import java.util.List;
import java.util.Map;

public interface HotelService {

    //    展示酒店评分的平均分、中位数、最高分、最低分、众数，以数据集中“hotel_score”属性为依据进行展示
    Map<String, Object> getNums();

    //    展示酒店评分与房间价格的关系，按照数据集中“hotel_score”属性、
//            “hotel_grade_text”属性和“room_price”属性进行统计，
//    每个酒店都有不同的房间价格，可以取其最贵的房间价格进行统计
    List<String> getDiffGrades();

    List<ScorePrice> scoreAndPrice(String hotelGrade);


//    选择你关注的两个城市，按照数据集中“hotel_location_info”属性展示酒店分布热力图
    List<LocationInfo> getLocations(String city);

    Integer updateCoordinate(String city);

    List<String> getCities();

    List<Coordinate> getCoordinate(String city);

    List<CityHotelCount> countHotelsByCity(String grade);

    List<CityRoomCount> countRoomsByCity();

    List<CommentFrequency> analyzeHotelComments();

    List<AreaPrice> getRoomPriceByArea();

}
