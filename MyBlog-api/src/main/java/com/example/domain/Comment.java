package com.example.domain;

import lombok.Data;

@Data
public class Comment {
    private Long id;

    private String content;

    private Long createDate;

    //评论文章的id
    private Long articleId;
    //评论者的id
    private Long authorId;
    //一级评论的id
    private Long parentId;
    // 二级评论中给谁说的
    private Long toUid;
    //一级评论还是二级评论
        private Integer level;
}
