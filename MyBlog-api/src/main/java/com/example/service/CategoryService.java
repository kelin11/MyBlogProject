package com.example.service;

import com.example.dao.CategoryDao;
import com.example.vo.CategoryVo;
import com.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface CategoryService {


    CategoryVo findCategorys(Long categoryId);

    Result findAllCategorys();


    Result findAllDetail();


    Result findCategoriesListById(Long id);


}
