package com.example.springbootmybatis.service.impl;

import com.example.springbootmybatis.mapper.SubCardDao;
import com.example.springbootmybatis.po.SubCardEntity;
import com.example.springbootmybatis.service.SubCardService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("subCardService")
public class SubCardServiceImpl extends ServiceImpl<SubCardDao, SubCardEntity> implements SubCardService {



}