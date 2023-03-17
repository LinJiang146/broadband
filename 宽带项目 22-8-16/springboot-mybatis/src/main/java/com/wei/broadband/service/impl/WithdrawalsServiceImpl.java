package com.wei.broadband.service.impl;

import com.wei.broadband.mapper.WithdrawalsDao;
import com.wei.broadband.po.WithdrawalsEntity;
import com.wei.broadband.service.WithdrawalsService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("withdrawalsService")
public class WithdrawalsServiceImpl extends ServiceImpl<WithdrawalsDao, WithdrawalsEntity> implements WithdrawalsService {



}