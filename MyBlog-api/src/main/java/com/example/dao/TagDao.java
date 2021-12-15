package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagDao extends BaseMapper<Tag> {

    /**
     * 根据文章id查询tag列表
     * @param articleId
     * @return
     */


     List<Tag> findTagsByArticleId(Long articleId);
    /**
     * 查找最热标签
     *
     */
    List<Long> findHotListTagId(Integer size);
    /**
     * 根据tag——id查找tag——name
     */
    List<Tag> findTagNameByTagId(List<Long> tagIds);

}
