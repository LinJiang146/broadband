package com.example.springbootmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootmybatis.po.Text;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TextMapper extends BaseMapper<Text> {
}
