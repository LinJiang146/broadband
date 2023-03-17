package com.wei.broadband.service.impl;

import com.wei.broadband.mapper.WalletItemDao;
import com.wei.broadband.po.MissionItem;
import com.wei.broadband.service.WalletItemService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("walletItemService")
public class WalletItemServiceImpl extends ServiceImpl<WalletItemDao, MissionItem> implements WalletItemService {



}