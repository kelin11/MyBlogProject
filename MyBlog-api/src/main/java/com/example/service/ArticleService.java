package com.example.service;

import com.example.dao.dos.Archives;
import com.example.vo.ArticleVo;
import com.example.vo.Result;
import com.example.vo.params.ArticleParam;
import com.example.vo.params.PageParams;

import java.util.List;

public interface ArticleService {
    /**
     * 文章列表
     * @param pageParams
     * @return
     */
    Result listArticle (PageParams pageParams);

    /**
     * 查找最热文章
     * @param limit
     * @return
     */
    List<ArticleVo> hotArticle(int limit);
    /**
     * 查找最新文章
     */
    List<ArticleVo> newArticle(int limit);

    List<Archives> listArchives();

    ArticleVo findArticleById(Long id);

    Result publish(ArticleParam articleParam);

}
