package com.example.springbootmybatis.mapper;

import com.example.springbootmybatis.po.PaymentsEntity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author wei
 * @email 
 * @date 2023-02-19 14:07:31
 */
@Mapper
public interface PaymentsDao extends BaseMapper<PaymentsEntity> {
	
}
