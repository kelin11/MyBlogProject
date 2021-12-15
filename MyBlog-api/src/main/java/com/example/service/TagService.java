package com.example.service;

import com.example.vo.Result;
import com.example.vo.TagVo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TagService {
    /**
     * 根据articleid查找相应的标签集合
     * @param articleId
     * @return
     */
    List<TagVo> findTagsByArticleId(Long articleId);

    /**
     * 最热标签
     */
    List<TagVo> hot(Integer size);

    Result findAllTags();


    Result findTagsDetails();


    Result findTagsDetailsById(Long id);
}
