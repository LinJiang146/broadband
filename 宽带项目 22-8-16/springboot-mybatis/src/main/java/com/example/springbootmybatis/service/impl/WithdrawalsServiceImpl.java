package com.example.springbootmybatis.service.impl;

import com.example.springbootmybatis.mapper.WithdrawalsDao;
import com.example.springbootmybatis.po.WithdrawalsEntity;
import com.example.springbootmybatis.service.WithdrawalsService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.Map;

import com.example.springbootmybatis.service.WalletItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Service("withdrawalsService")
public class WithdrawalsServiceImpl extends ServiceImpl<WithdrawalsDao, WithdrawalsEntity> implements WithdrawalsService {



}