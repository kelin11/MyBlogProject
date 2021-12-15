package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentsDao extends BaseMapper<Comment> {

}
