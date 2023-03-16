package com.example.springbootmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootmybatis.po.Revenue;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RevenueMapper extends BaseMapper<Revenue> {
}
