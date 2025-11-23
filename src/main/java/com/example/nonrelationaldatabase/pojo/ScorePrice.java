package com.example.nonrelationaldatabase.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ScorePrice {
    private Double score;
    private List<HotelPrice> hotels;
}
