package com.example.springbootmybatis.mapper;

import com.example.springbootmybatis.po.MissionEntity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author wei
 * @email 
 * @date 2023-02-22 20:06:31
 */
@Mapper
public interface MissionDao extends BaseMapper<MissionEntity> {
	
}
