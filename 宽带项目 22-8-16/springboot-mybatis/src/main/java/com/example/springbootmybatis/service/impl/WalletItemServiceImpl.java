package com.example.springbootmybatis.service.impl;

import com.example.springbootmybatis.mapper.WalletItemDao;
import com.example.springbootmybatis.po.MissionItem;
import com.example.springbootmybatis.service.WalletItemService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("walletItemService")
public class WalletItemServiceImpl extends ServiceImpl<WalletItemDao, MissionItem> implements WalletItemService {



}