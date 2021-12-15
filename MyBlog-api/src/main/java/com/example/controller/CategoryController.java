package com.example.controller;

import com.example.service.CategoryService;
import com.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result getCategorys(){
        return categoryService.findAllCategorys();
    }

    @GetMapping("/detail")
    public Result Categoriesdetails(){
        return categoryService.findAllDetail();
    }
    @GetMapping("/detail/{id}")
    public Result CategoriesdetailByid(@PathVariable("id") Long id){
        return categoryService.findCategoriesListById(id);
    }



}
