package com.example.springbootmybatis.service.impl;

import com.example.springbootmybatis.mapper.PaymentsDao;
import com.example.springbootmybatis.po.PaymentsEntity;
import com.example.springbootmybatis.service.PaymentsService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("paymentsService")
public class PaymentsServiceImpl extends ServiceImpl<PaymentsDao, PaymentsEntity> implements PaymentsService {



}