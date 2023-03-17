package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wei.broadband.mapper.CardCustomerMapper;
import com.wei.broadband.po.CardCustomer;
import com.wei.broadband.service.CardCustomerService;
import org.springframework.stereotype.Service;

@Service
public class CardCustomerServiceImpl extends ServiceImpl<CardCustomerMapper, CardCustomer> implements CardCustomerService {
}
