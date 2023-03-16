package com.example.springbootmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootmybatis.po.BroadbandCustomer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BroadbandCustomerMapper extends BaseMapper<BroadbandCustomer> {
}
