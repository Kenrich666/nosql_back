package com.example.nonrelationaldatabase.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class CommentFrequency {
    @Field("_id")
    private String interval;
    @Field("comments")
    private List<Comment> comments;
}

@Data
class Comment{
    @Field("comment")
    private String comment;
    @Field("count")
    private Integer count;
}
