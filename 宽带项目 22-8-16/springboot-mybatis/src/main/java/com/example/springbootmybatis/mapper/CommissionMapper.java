package com.example.springbootmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootmybatis.po.Commission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommissionMapper extends BaseMapper<Commission> {
}
