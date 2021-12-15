package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dao.CategoryDao;
import com.example.domain.Category;
import com.example.service.CategoryService;
import com.example.vo.CategoryVo;
import com.example.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceimpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;


    @Override
    public CategoryVo findCategorys(Long categoryId) {
        Category category = categoryDao.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public Result findAllCategorys() {

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryDao.selectList(queryWrapper);
        List<CategoryVo> categoryVos = copylist(categories);
        return Result.success(categoryVos);


    }

    /**
     * 发布文章的时候显示所有的类别以便于我们去添加的功能
     * @return
     */
    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        List<Category> categories = categoryDao.selectList(queryWrapper);
        List<CategoryVo> categoryVos = copylist(categories);
        return Result.success(categoryVos);
    }

    /**
     * 文章分类列表功能
     * @param id
     * @return
     */
    @Override
    public Result findCategoriesListById(Long id) {
        Category category = categoryDao.selectById(id);
        return Result.success(copy(category));
    }

    private List<CategoryVo> copylist(List<Category> categories) {
        List<CategoryVo> result=  new ArrayList<>();
        for (Category category : categories) {
            CategoryVo categoryVo = new CategoryVo();
             categoryVo= copy(category);
             result.add(categoryVo);
        }
        return result;
    }

    private CategoryVo copy(Category category) {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category,categoryVo);
            return categoryVo;
    }
}
