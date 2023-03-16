package com.example.springbootmybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmybatis.mapper.CustomerBillingMapper;
import com.example.springbootmybatis.po.CustomerBilling;
import com.example.springbootmybatis.service.CustomerBillingService;
import org.springframework.stereotype.Service;
import java.util.Map;



@Service("customerBillingService")
public class CustomerBillingServiceImpl extends ServiceImpl<CustomerBillingMapper, CustomerBilling> implements CustomerBillingService {

}