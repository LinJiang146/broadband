package com.example.springbootmybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmybatis.mapper.MaintainMapper;
import com.example.springbootmybatis.po.Maintain;
import com.example.springbootmybatis.service.MaintainService;
import org.springframework.stereotype.Service;

@Service
public class MaintainServiceImpl extends ServiceImpl<MaintainMapper, Maintain> implements MaintainService {
}
