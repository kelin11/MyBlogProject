package com.example.vo.params;

import lombok.Data;

@Data
public class PageParams {
    private int page = 1;
    private int pagesize = 10;
    private Long categoryId;
    private Long tagId;
    //以下是: 对文章归档功能的实现的 pageparam 参数类中需要添加的实体类的 参数

    private String year;

    private String month;

    public String getMonth(){
        if (this.month != null && this.month.length() == 1){
            return "0"+this.month;
        }
        return this.month;
    }
}
