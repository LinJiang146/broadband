package com.example.springbootmybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmybatis.mapper.PackageMapper;
import com.example.springbootmybatis.po.Package;
import com.example.springbootmybatis.service.PackageService;
import org.springframework.stereotype.Service;

@Service
public class PackageServiceImpl extends ServiceImpl<PackageMapper, Package> implements PackageService {
}
