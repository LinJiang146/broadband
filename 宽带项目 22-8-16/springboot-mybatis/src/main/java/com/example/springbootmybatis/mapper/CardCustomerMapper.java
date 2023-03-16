package com.example.springbootmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootmybatis.po.CardCustomer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CardCustomerMapper extends BaseMapper<CardCustomer> {
}
