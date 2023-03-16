package com.example.springbootmybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmybatis.mapper.UserMapper;
import com.example.springbootmybatis.po.User;
import com.example.springbootmybatis.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
