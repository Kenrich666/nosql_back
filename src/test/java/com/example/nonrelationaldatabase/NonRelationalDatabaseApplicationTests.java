package com.example.nonrelationaldatabase;

import com.example.nonrelationaldatabase.controller.HotelController;
import com.example.nonrelationaldatabase.dao.HotelRepository;
import com.example.nonrelationaldatabase.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class NonRelationalDatabaseApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelController hotelController;

    @Autowired
    private HotelService hotelService;

    @Test
    void contextLoads() {
//        System.out.println(hotelRepository.getMode());
//        System.out.println(hotelRepository.getMedian());
//        hotelRepository.getMedian();
//        System.out.println(mongoTemplate.findAll(Hotel.class, "information"));
//        System.out.println(hotelRepository.getHotels("高档型"));
//        System.out.println(hotelRepository.getGrades());
//        System.out.println(hotelRepository.getLocations("北京"));
//        System.out.println(hotelController.getNums());
//        System.out.println(hotelService.updateCoordinate("天津"));
//        System.out.println(hotelRepository.getCoordinate("天津"));
//        System.out.println(hotelRepository.countHotelsByCity());
//        System.out.println(hotelRepository.countRoomsByCity());
//        System.out.println(hotelRepository.analyzeHotelComments());
//        System.out.println(hotelRepository.getCities());
        System.out.println(hotelRepository.getRoomPriceByArea().get(1));
    }

    @Test
    void testConnection() {
        try {
            // 尝试查询一个已知的集合
            long count = mongoTemplate.getCollection("information").countDocuments();
            System.out.println(count); // 如果查询成功，则返回 true
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
            System.out.println("false"); // 连接失败
        }
    }

}
