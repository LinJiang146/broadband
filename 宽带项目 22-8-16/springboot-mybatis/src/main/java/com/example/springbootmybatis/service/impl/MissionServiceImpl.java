package com.example.springbootmybatis.service.impl;

import com.example.springbootmybatis.mapper.MissionDao;
import com.example.springbootmybatis.po.MissionEntity;
import com.example.springbootmybatis.service.MissionService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("missionService")
public class MissionServiceImpl extends ServiceImpl<MissionDao, MissionEntity> implements MissionService {

}