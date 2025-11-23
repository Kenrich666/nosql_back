package com.example.nonrelationaldatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProxyController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/examples")
    public ResponseEntity<String> getBaiduMapData() {
        String url = "https://echarts.apache.org/examples/data/asset/data/hangzhou-tracks.json";

        // 使用 RestTemplate 代理请求
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}
