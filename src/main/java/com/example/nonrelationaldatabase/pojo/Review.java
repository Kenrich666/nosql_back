package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 用户评论实体类
 */
@Data
public class Review {

    @Field("user_name")
    private String userName;

    @Field("user_rating")
    private Integer userRating; // 样例中为整数，如需支持半星可改为 Double

    @Field("publish_info")
    private String publishInfo;

    @Field("likes")
    private Integer likes;

    @Field("content")
    private String content;
}