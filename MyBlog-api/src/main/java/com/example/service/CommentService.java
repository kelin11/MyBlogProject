package com.example.service;


import com.example.vo.Result;
import com.example.vo.params.CommentParam;

public interface CommentService {

    Result CommentsByArticleId(Long ArticleId);

    Result addComments(CommentParam commentParam);


}
