package com.wei.broadband.service.impl;

import com.wei.broadband.mapper.MissionDao;
import com.wei.broadband.po.MissionEntity;
import com.wei.broadband.service.MissionService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("missionService")
public class MissionServiceImpl extends ServiceImpl<MissionDao, MissionEntity> implements MissionService {

}