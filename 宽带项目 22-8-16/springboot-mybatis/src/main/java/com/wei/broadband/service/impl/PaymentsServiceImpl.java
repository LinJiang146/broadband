package com.wei.broadband.service.impl;

import com.wei.broadband.mapper.PaymentsDao;
import com.wei.broadband.po.PaymentsEntity;
import com.wei.broadband.service.PaymentsService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("paymentsService")
public class PaymentsServiceImpl extends ServiceImpl<PaymentsDao, PaymentsEntity> implements PaymentsService {



}