package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.broadband.mapper.CommissionMapper;
import com.wei.broadband.po.Commission;
import com.wei.broadband.service.CommissionService;
import org.springframework.stereotype.Service;

@Service
public class CommissionServiceImpl extends ServiceImpl<CommissionMapper, Commission> implements CommissionService {
}
