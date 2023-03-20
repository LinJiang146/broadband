package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.broadband.common.R;
import com.wei.broadband.mapper.PackageMapper;
import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.CardCustomer;
import com.wei.broadband.po.Package;
import com.wei.broadband.service.BroadbandCustomerService;
import com.wei.broadband.service.CardCustomerService;
import com.wei.broadband.service.PackageService;
import com.wei.broadband.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageServiceImpl extends ServiceImpl<PackageMapper, Package> implements PackageService {


    @Autowired
    private UserService userService;

    @Autowired
    private BroadbandCustomerService broadbandCustomerService;

    @Autowired
    private CardCustomerService cardCustomerService;

    @Override
    public R<String> savePackage(int id, Package pack) {
        int permission = userService.getById(id).getPermission();
        if(permission<=2) {
            pack.setCreateUser(id);
            pack.setUpdateUser(id);
            save(pack);
            return R.success("新增套餐成功");
        }
        return R.error("权限不足");
    }

    @Override
    public R<List<Package>> packageList(String content, Integer sort, Integer type) {
        LambdaQueryWrapper<Package> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(content)) {
            queryWrapper.eq(type!=null,Package::getType,type).like(Package::getName, content).or().
                    like(Package::getPrice, content).or().
                    like(Package::getDescription, content);
        }
        else {
            queryWrapper.eq(type!=null,Package::getType,type);
        }
        if (sort!=null){
            if(sort==0)
                queryWrapper.orderByDesc(Package::getUpdateTime);
            if(sort==1)
                queryWrapper.orderByAsc(Package::getUpdateTime);
        }

        List<Package> packages = list(queryWrapper);
        return R.success(packages);
    }

    @Override
    public R<String> updataPackage(int id, Package pack) {
        int permission = userService.getById(id).getPermission();
        if(permission<=2) {
            pack.setUpdateUser(id);
            updateById(pack);
            return R.success("更新成功");
        }
        return R.error("权限不足");
    }

    @Override
    public R<String> deletePackage(int userid, int id) {
        int permission = userService.getById(userid).getPermission();
        if(permission<=2) {
            CardCustomer c = cardCustomerService.getOne(new LambdaQueryWrapper<CardCustomer>().eq(CardCustomer::getPackageId,id));
            BroadbandCustomer b = broadbandCustomerService.getOne(new LambdaQueryWrapper<BroadbandCustomer>().eq(BroadbandCustomer::getPackageId,id));
            if(b==null){
                if(c==null){
                    removeById(id);
                    return R.success("删除成功");
                }
                else return R.error(c.getName()+"客户正使用该套餐，无法删除");
            }
            else return R.error(b.getName()+"客户正使用该套餐，无法删除");
        }
        return R.error("权限不足");
    }
}
