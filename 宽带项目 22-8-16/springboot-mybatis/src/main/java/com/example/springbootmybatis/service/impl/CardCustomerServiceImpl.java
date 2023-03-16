package com.example.springbootmybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.springbootmybatis.mapper.CardCustomerMapper;
import com.example.springbootmybatis.po.CardCustomer;
import com.example.springbootmybatis.service.CardCustomerService;
import org.springframework.stereotype.Service;

@Service
public class CardCustomerServiceImpl extends ServiceImpl<CardCustomerMapper, CardCustomer> implements CardCustomerService {
}
