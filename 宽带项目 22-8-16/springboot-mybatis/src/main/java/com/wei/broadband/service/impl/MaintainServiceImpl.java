package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.broadband.mapper.MaintainMapper;
import com.wei.broadband.po.Maintain;
import com.wei.broadband.service.MaintainService;
import org.springframework.stereotype.Service;

@Service
public class MaintainServiceImpl extends ServiceImpl<MaintainMapper, Maintain> implements MaintainService {
}
