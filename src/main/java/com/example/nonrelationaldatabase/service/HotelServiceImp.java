package com.example.nonrelationaldatabase.service;

import com.example.nonrelationaldatabase.dao.HotelRepository;
import com.example.nonrelationaldatabase.pojo.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HotelServiceImp implements HotelService {

    private final HotelRepository hotelRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public HotelServiceImp(HotelRepository mongoDao, RestTemplate restTemplate) {
        this.hotelRepository = mongoDao;
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, Object> getNums() {
        Map<String, Object> map = new HashMap<>();
//        最大、最小、平均值
        MaxMinAvg maxMinAvg = hotelRepository.getMaxMinAvg();
//        众数
        List<Mode> mode = hotelRepository.getMode();

        List<Scores> list = hotelRepository.getMedian();
        int len = list.size();
        Double median;
        if (len % 2 == 0)
            median = (list.get((len - 1) / 2).getScore() + list.get(len / 2).getScore()) / 2;
        else median = list.get(len / 2).getScore();

        map.put("max", maxMinAvg.getMaxScore());
        map.put("min", maxMinAvg.getMinScore());
        map.put("avg", maxMinAvg.getAvgScore());
        map.put("mode", mode);
        map.put("median", median);

        return map;
    }

    @Override
    public List<String> getDiffGrades() {
        return hotelRepository.getDiffGrades();
    }

    @Override
    public List<ScorePrice> scoreAndPrice(String hotelGrade) {
        return hotelRepository.getHotels(hotelGrade);
    }

    @Override
    public List<LocationInfo> getLocations(String city) {
        return hotelRepository.getLocations(city);
    }

    @Override
    public Integer updateCoordinate(String city) {
        List<LocationInfo> locations = hotelRepository.getLocations(city);
        for (int i = 0; i < locations.size(); i++) {
            try {
                if (i != 0 && i % 150 == 0)
                    Thread.sleep(1000);
                String url = "https://api.map.baidu.com/geocoding/v3/?address="
                        + locations.get(i).getName() + "&output=json&ak=CSsTZw7EWLXZJmEmvXmNm0LxvzcrTUKM";
                // 发送GET请求到百度地图API
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    try {
                        // 解析JSON响应
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                        if (jsonResponse.get("result") != null) {
                            JsonNode result = jsonResponse.get("result");
                            JsonNode location = result.get("location");
                            double lat = location.get("lat").asDouble();
                            double lng = location.get("lng").asDouble();
                            hotelRepository.updateCoordinate(locations.get(i).getName(), lat, lng);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return -1;
                    }
                } else {
                    return -1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                return -1;
            }
        }
        return 1;
    }

    @Override
    public List<String> getCities() {
        return hotelRepository.getCities();
    }

    @Override
    public List<Coordinate> getCoordinate(String city) {
        return hotelRepository.getCoordinate(city);
    }

    @Override
    public List<CityHotelCount> countHotelsByCity(String grade) {
        return hotelRepository.countHotelsByCity(grade);
    }

    @Override
    public List<CityRoomCount> countRoomsByCity() {
        return hotelRepository.countRoomsByCity();
    }

    @Override
    public List<CommentFrequency> analyzeHotelComments() {
        return hotelRepository.analyzeHotelComments();
    }

    @Override
    public List<AreaPrice> getRoomPriceByArea() {
        return hotelRepository.getRoomPriceByArea();
    }
}
