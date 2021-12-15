package com.example.controller;

import com.example.service.CommentService;
import com.example.vo.Result;
import com.example.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/article/{id}")
    public Result comments(@PathVariable("id") Long articleId){
        return commentService.CommentsByArticleId(articleId);

    }
    @PostMapping("/create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.addComments(commentParam);
    }
}
