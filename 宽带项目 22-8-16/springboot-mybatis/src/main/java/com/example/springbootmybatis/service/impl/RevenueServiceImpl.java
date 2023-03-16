package com.example.springbootmybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmybatis.mapper.RevenueMapper;

import com.example.springbootmybatis.po.Revenue;
import com.example.springbootmybatis.service.RevenueService;
import org.springframework.stereotype.Service;

@Service
public class RevenueServiceImpl extends ServiceImpl<RevenueMapper, Revenue> implements RevenueService {
}
