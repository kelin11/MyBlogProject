package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dao.dos.Archives;
import com.example.domain.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleDao extends BaseMapper<Article> {

    List<Archives> listArchives();

    List<Article> listArticle(Page<Article> page,
                              Long categoryId,
                              Long tagId,
                              String year,
                              String month);



}
