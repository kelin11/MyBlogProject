package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dao.ArticleDao;
import com.example.domain.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    /**
     * 更新文章阅读次数view
     * @param articleDao
     * @param article
     * 同时期望该操作在线程池中执行
     */
    @Async("taskExecutor")//调用我们设置的线程
    public void updateArticleViewCount(ArticleDao articleDao, Article article) {
            Article articleupdate  = new Article();
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            articleupdate.setViewCounts(article.getViewCounts()+1);
            queryWrapper.eq(Article::getId,article.getId()).eq(Article::getViewCounts,article.getViewCounts());
            articleDao.update(articleupdate,queryWrapper);
        try {
            //在前5s 我这个现成 不参与系统资源的抢占   意思就是 5s控制后 我才可能viewCounts++;
            Thread.sleep(5000);
            System.out.println("更新完成了" );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
