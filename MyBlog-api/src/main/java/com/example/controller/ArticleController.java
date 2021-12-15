package com.example.controller;



import com.example.common.aop.LogAnnotation;
import com.example.domain.Article;
import com.example.service.ArticleService;
import com.example.service.ThreadService;
import com.example.vo.ArticleVo;
import com.example.vo.Result;
import com.example.vo.params.ArticleParam;
import com.example.vo.params.PageParams;
import io.lettuce.core.api.sync.RedisSyncCommandsExtensionsKt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController//json数据 进行交互
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;


    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    //加上次注解,代表对此接口进行记录日志 AOP
    //现在实现对归档文章列表的处理

    @LogAnnotation(module="文章",operator="查找文章列表")
    public Result listArticle(@RequestBody PageParams pageParams){
        //向前端返回数据
        return articleService.listArticle(pageParams);
    }
    @PostMapping("/hot")
    public Result hotArticle(){
        int limit = 5; //显示阅读数量最多的前五个文章
        return Result.success(articleService.hotArticle(limit));
    }
    @PostMapping("/new")
    public Result newArticle(){
        int limit = 5;//最新的五篇文章
        return Result.success(articleService.newArticle(limit));
    }

    /**
     * 文章归档
     * @return
     */
    @PostMapping("/listArchives")
    public Result listArchives(){
        return Result.success(articleService.listArchives());
    }

    /**
     *  查看文章详情
     * @param id
     * @return
     */
    @Autowired
    private ThreadService threadService;


    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long id){
        /**
         * 1.根据Articleid查询
         * 2.根据bodyId 和 CateGoryId 做关联查询
         *
         */
        ArticleVo articleVo = articleService.findArticleById(id);
        return  Result.success(articleVo);
    }
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }


}
