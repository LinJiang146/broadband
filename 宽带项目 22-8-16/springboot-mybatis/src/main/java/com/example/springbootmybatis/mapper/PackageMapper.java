package com.example.springbootmybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootmybatis.po.Package;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PackageMapper extends BaseMapper<Package> {
}
