package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.domain.SysUser;
import com.example.vo.ArticleVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserDao  extends BaseMapper<SysUser> {



}
