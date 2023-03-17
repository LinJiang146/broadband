package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.broadband.mapper.RevenueMapper;

import com.wei.broadband.po.Revenue;
import com.wei.broadband.service.RevenueService;
import org.springframework.stereotype.Service;

@Service
public class RevenueServiceImpl extends ServiceImpl<RevenueMapper, Revenue> implements RevenueService {
}
