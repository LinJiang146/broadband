package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.broadband.mapper.PackageMapper;
import com.wei.broadband.po.Package;
import com.wei.broadband.service.PackageService;
import org.springframework.stereotype.Service;

@Service
public class PackageServiceImpl extends ServiceImpl<PackageMapper, Package> implements PackageService {
}
