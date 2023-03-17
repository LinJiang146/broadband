package com.wei.broadband.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wei.broadband.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where id = 1")
    User getuser(int id);
}
