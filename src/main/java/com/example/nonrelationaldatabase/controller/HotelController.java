package com.example.nonrelationaldatabase.controller;

import com.example.nonrelationaldatabase.pojo.*;
import com.example.nonrelationaldatabase.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }


    @RequestMapping("/nums")
    public Map<String, Object> getNums() {
        return hotelService.getNums();
    }

    @RequestMapping("/grade")
    public List<String> getAllGrades() {
        return hotelService.getDiffGrades();
    }

    @RequestMapping(value = {"/scoreAndPrice/{grade}", "/scoreAndPrice/"})
    public List<ScorePrice> getScoreAndPrice(@PathVariable(required = false) String grade) {
        return hotelService.scoreAndPrice(grade);
    }

    @RequestMapping("/locations/{city}")
    public List<LocationInfo> getLocations(@PathVariable String city) {
//        System.out.println(city);
        return hotelService.getLocations(city);
    }

    @RequestMapping("/coordinate/{city}")
    public List<Coordinate> getCoordinate(@PathVariable String city) {
//        System.out.println(city);
        return hotelService.getCoordinate(city);
    }

    @RequestMapping("/cities")
    public List<String> getCities() {
        return hotelService.getCities();
    }

    @RequestMapping(value = {"/countHotels/{grade}", "/countHotels/"})
    public List<CityHotelCount> countHotelsByCity(@PathVariable(required = false) String grade) {
        return hotelService.countHotelsByCity(grade);
    }

    @RequestMapping("/countRooms")
    public List<CityRoomCount> countRoomsByCity() {
//        System.out.println(city);
        return hotelService.countRoomsByCity();
    }

    @RequestMapping("/commentFrequency")
    public List<CommentFrequency> analyzeHotelComments() {
        return hotelService.analyzeHotelComments();
    }

    @RequestMapping("/areaPrice")
    public List<AreaPrice> getRoomPriceByArea() {
        return hotelService.getRoomPriceByArea();
    }


}
