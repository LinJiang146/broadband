package com.wei.broadband.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.broadband.common.R;
import com.wei.broadband.po.Package;

import java.util.List;

public interface PackageService extends IService<Package> {

    R<String> savePackage(int id, Package pack);

    R<List<Package>> packageList(String content, Integer sort, Integer type);

    R<String> updataPackage(int id, Package pack);

    R<String> deletePackage(int userid, int id);
}
