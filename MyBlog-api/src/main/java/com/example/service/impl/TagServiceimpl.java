package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dao.TagDao;
import com.example.domain.Tag;
import com.example.service.TagService;
import com.example.vo.Result;
import com.example.vo.TagVo;
import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceimpl implements TagService {
    @Autowired
    private TagDao tagDao;


    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        //mybatis-plus 无法进行多表查询, 使用我们自己的select
        List<Tag> tags = tagDao.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public List<TagVo> hot(Integer size) {
        /**
         * 思路:在kelin_article_tag标签中 对article_id进行count() 计数并且desc排序groupbby 随后 再查找对应的tag_id 在通过tag_id找到对应的tag_name
         *
         */
        List<Long> hotListTagIds = tagDao.findHotListTagId(size);
        //判断我们获取到的hotListTagIds是否为空
        if(CollectionUtils.isEmpty(hotListTagIds)){
            return Collections.emptyList();
        }
        List<Tag> TagNames = tagDao.findTagNameByTagId(hotListTagIds);
        return copyList(TagNames);
    }

    /**
     * 发布文章的时候显示所有的标签 让他添加的功能
     * @return
     */
    @Override
    public Result findAllTags() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = tagDao.selectList(queryWrapper);
        List<TagVo> tagVos = copyList(tags);
        return Result.success(tagVos);
    }

    @Override
    public Result findTagsDetails() {
        //查找所有的标签
        return Result.success(copyList(tagDao.selectList(new LambdaQueryWrapper<Tag>())));
    }

    /**
     * 文章标签归档功能
     * @param id
     * @return
     */
    @Override
    public Result findTagsDetailsById(Long id) {
        Tag tag = tagDao.selectById(id);
        return Result.success(copy(tag));
    }

    private List<TagVo> copyList(List<Tag> tags){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tags) {
              TagVo tagVo= copy(tag);
              tagVoList.add(tagVo);
        }
        return tagVoList;
    }
    private TagVo copy(Tag tag){
        TagVo tagVo= new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
}
