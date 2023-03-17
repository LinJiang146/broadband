package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.broadband.mapper.CustomerBillingMapper;
import com.wei.broadband.po.CustomerBilling;
import com.wei.broadband.service.CustomerBillingService;
import org.springframework.stereotype.Service;


@Service("customerBillingService")
public class CustomerBillingServiceImpl extends ServiceImpl<CustomerBillingMapper, CustomerBilling> implements CustomerBillingService {

}