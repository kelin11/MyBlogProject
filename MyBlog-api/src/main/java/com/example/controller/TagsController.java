package com.example.controller;

import com.example.service.TagService;
import com.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagsController {
    @Autowired
    private TagService tagService;

    /**
     * 最热标签: 在kelin_article_tag中tag_id 对应的最多的articleid
     * @return
     */
    @GetMapping("/hot")
    public Result listHotTags(){
        //最热标签框里面显示的最大数量  : limit
        int limit  = 6;
       return  Result.success(tagService.hot(limit));
    }
    @GetMapping
    public Result tags(){
        return tagService.findAllTags();
    }
    @GetMapping("/detail")
    public Result detail(){
        return tagService.findTagsDetails();
    }
    @GetMapping("/detail/{id}")
    public Result findTagDetailsById(@PathVariable("id") Long id){
        return tagService.findTagsDetailsById(id);
    }
}
