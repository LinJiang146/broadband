package com.example.springbootmybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmybatis.mapper.CommissionMapper;
import com.example.springbootmybatis.po.Commission;
import com.example.springbootmybatis.service.CommissionService;
import org.springframework.stereotype.Service;

@Service
public class CommissionServiceImpl extends ServiceImpl<CommissionMapper, Commission> implements CommissionService {
}
