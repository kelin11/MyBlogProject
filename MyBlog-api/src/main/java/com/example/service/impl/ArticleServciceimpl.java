package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dao.ArticleBodyDao;
import com.example.dao.ArticleDao;
import com.example.dao.ArticleTagDao;
import com.example.dao.dos.Archives;
import com.example.domain.Article;
import com.example.domain.ArticleBody;
import com.example.domain.ArticleTag;
import com.example.domain.SysUser;
import com.example.service.*;
import com.example.utils.UserThreadLocal;
import com.example.vo.*;
import com.example.vo.params.ArticleBodyParam;
import com.example.vo.params.ArticleParam;
import com.example.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServciceimpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagDao articleTagDao;
    @Autowired
    private ArticleBodyDao articleBodyDao;


    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPagesize());
        List<Article> resultlist= articleDao.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(resultlist,true,true,false,false));

     /*   *//**
         * 1.分页查询article数据库的表
         *//*
        //获取相应的页数page
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPagesize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //是否置顶排序
        //order by create_date desc
        if(pageParams.getCategoryId()!=null){
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }
        List<Long> articleTagidlist = new ArrayList<>();
        if(pageParams.getTagId()!=null){
            Long tagId = pageParams.getTagId();
            LambdaQueryWrapper<ArticleTag> queryWrapper2 = new LambdaQueryWrapper();
            queryWrapper2.select(ArticleTag::getArticleId).eq(ArticleTag::getTagId,tagId);
            List<ArticleTag> articleTags = articleTagDao.selectList(queryWrapper2);
            for (ArticleTag articleTag : articleTags) {
                articleTagidlist.add(articleTag.getArticleId());
            }
            if(articleTags.size()>0){
                queryWrapper.in(Article::getId,articleTagidlist);
            }
        }
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        Page<Article> pagelist = articleDao.selectPage(page, queryWrapper);
        List<Article> records = pagelist.getRecords();
        List<ArticleVo> articleVoList = copyList(records,true,true,false,true);
        return  Result.success(articleVoList);*/
    }

    /**
     * 将list<Artilcle>=>List<ArticleVo>  返回值是 ArticleVo 逻辑层 与 表现层 的Article类型交互结果
     * @param records
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody, boolean isCategory) {
        List<ArticleVo> list = new ArrayList<>();
        for (Article record : records) {
           list.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return list;
    }

    /**
     * 将 Article 类型 转化为=>Articlevo 类型
     * @param article
     * @return
     */
    private ArticleVo copy(Article article ,boolean isTag,boolean isAuthor,boolean isBody, boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        //由于 ArticleVo和Article实体类的设置的 参数createdata类型不同 BeanUtils不能转换
        //所以我们自己进行一个 类型的转换Article->ArticleVo (将逻辑层与持久层的交互 转化为 逻辑层 与表现层 的交互)
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的标签都需要tag 作者信息两个属性的 ,我们设置isAuthor isTag来判断
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            //得到作者的id
            Long authorId = article.getAuthorId();

            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if(isBody){
          Long BodyId  = article.getBodyId();
          articleVo.setBody(findArticleBodyById(BodyId));
        }
        if(isCategory){
          CategoryVo categoryVo =  findArticleCategoryById(article.getCategoryId());
          articleVo.setCategory(categoryVo);
        }

        return articleVo;
    }

    /**
     * Article categoryId ->Category->ArticleVo
     * @param categoryId
     * @return
     */
    @Autowired
    private CategoryService categoryService;

    private CategoryVo findArticleCategoryById(Long categoryId) {
        return categoryService.findCategorys(categoryId);
    }

    /**
     *articleid->bodyid->body->articleVo
     * @param bodyId
     * @return
     */
    @Autowired
    private ArticleBodyDao bodyDao;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getId,bodyId);
        ArticleBody articleBody = bodyDao.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo =new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }


    

    /**
     * 实现最热文章功能
     * @return
     */
    public List<ArticleVo> hotArticle(int limit){
        LambdaQueryWrapper<Article> queryWrapper  = new LambdaQueryWrapper();
        //mybatis-plus 直接使用
       queryWrapper.select(Article::getId,Article::getTitle).orderByDesc(Article::getViewCounts).last("limit "+limit);
        List<Article> articles = articleDao.selectList(queryWrapper);
        List<ArticleVo> articleVoList = copyList(articles, false, false,false,false);
        return articleVoList;
    }

    /**
     * 实现最新文章功能
     * @param limit
     * @return
     */
    @Override
    public List<ArticleVo> newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.select(Article::getId,Article::getTitle).orderByDesc(Article::getCreateDate).last("limit "+limit);
        List<Article> articles = articleDao.selectList(queryWrapper);
        List<ArticleVo> articleVoList = copyList(articles, false, false,false,false);
        return articleVoList;
    }

    /**
     * 文章归档
     * @return
     */
    @Override
    public List<Archives> listArchives() {
        List<Archives> archivesList = articleDao.listArchives();
        return archivesList;
    }


    @Autowired
    private ThreadService threadService;

    /**
     * 文章id-> Article-> ArticleVo   同时我们在第二线程中添加增加阅读次数的操作
     * @param id
     * @return
     */

    @Override
    public ArticleVo findArticleById(Long id) {
        Article article = articleDao.selectById(id);
        ArticleVo articleVo = copy(article,true,true,true,true);
        //再查看完成文章之后 ，应该返回数据，这个时候做一个更新操作，更写加写锁 ，就会阻塞其他的读取的操作
        //更新增加了此次接口的耗费时间
         //优化目标:在阅读数量增加的时候 我们不能因为他的阅读数量更新失败 就不让看文章 也就是不能影响我看文章的操作
        //优化: 线程池 将更新操作放到线程池中去执行 和主线程不相关
        threadService.updateArticleViewCount(articleDao,article);
        return articleVo;
    }

    @Override
    public Result publish(ArticleParam articleParam) {

        SysUser sysUser = UserThreadLocal.get();
        //文章数据库添加
        Article article = new Article();
        article.setId(articleParam.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setViewCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setCreateDate(System.currentTimeMillis());
        article.setTitle(articleParam.getTitle());
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        article.setCommentCounts(0);
        articleDao.insert(article);

        //文章标签数据库添加
        List<TagVo> tags = articleParam.getTags();
        if(tags!=null){
            for (TagVo tag : tags) {
                ArticleTag articleTag  = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagDao.insert(articleTag);
            }
        }
        //文章内容添加
        ArticleBodyParam body = articleParam.getBody();
        ArticleBody articleBody =new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(body.getContent());
        articleBody.setContentHtml(body.getContentHtml());
        articleBodyDao.insert(articleBody);

        //将article数据 和 之前添加的ArticleBody中文中内容的数据 使用id连接起来
        article.setBodyId(articleBody.getId());
        articleDao.updateById(article);
        //返回的是 : 新建的article的 id
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
        }
        


}
