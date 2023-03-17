package com.wei.broadband.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.R;
import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.CardCustomer;
import com.wei.broadband.po.Package;

import com.wei.broadband.service.BroadbandCustomerService;
import com.wei.broadband.service.CardCustomerService;
import com.wei.broadband.service.PackageService;
import com.wei.broadband.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/package")
@Slf4j
public class PackageController {
    @Autowired
    private PackageService packageService;

    @Autowired
    private UserService userService;

    @Autowired
    private BroadbandCustomerService broadbandCustomerService;

    @Autowired
    private CardCustomerService cardCustomerService;

    @PostMapping("/add")
    public R<String> save(HttpServletRequest request, @RequestBody Package pack) {
        int id = (int) request.getSession().getAttribute("user");
        int permission = userService.getById(id).getPermission();
        if(permission<=2) {
            pack.setCreateUser(id);
            pack.setUpdateUser(id);
            packageService.save(pack);
            return R.success("新增套餐成功");
        }
        return R.error("权限不足");
    }

    @GetMapping("/lists")
    public R<List<Package>> list(String content,int sort,int type){
        LambdaQueryWrapper<Package> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(content)) {
            queryWrapper.eq(Package::getType,type).like(Package::getName, content).or().
                    like(Package::getPrice, content).or().
                    like(Package::getDescription, content);
        }
        else {
            queryWrapper.eq(Package::getType,type);
        }
        if(sort==0)
            queryWrapper.orderByDesc(Package::getUpdateTime);
        if(sort==1)
            queryWrapper.orderByAsc(Package::getUpdateTime);
        List<Package> packages = packageService.list(queryWrapper);
        return R.success(packages);
    }
    @GetMapping("/list")
    public R<List<Package>> list(int type){
        LambdaQueryWrapper<Package> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Package::getType,type);
        List<Package> packages = packageService.list(queryWrapper);
        return R.success(packages);
    }
    @GetMapping("/alllist")
    public R<List<Package>> allList(){
        List<Package> packages = packageService.list();
        return R.success(packages);
    }

    @GetMapping("/getbyid")
    public R<Package> getById(int id){
        Package pack = packageService.getById(id);
        return R.success(pack);
    }

    @PostMapping("/updata")
    public R<String> updata(HttpServletRequest request,@RequestBody Package pack){

        int id = (int)request.getSession().getAttribute("user");
        int permission = userService.getById(id).getPermission();
        if(permission<=2) {
            pack.setUpdateUser(id);
            packageService.updateById(pack);
            return R.success("更新成功");
        }
        return R.error("权限不足");


    }
    @DeleteMapping
    public R<String> delete(HttpServletRequest request,int id){


        int userid = (int)request.getSession().getAttribute("user");
        int permission = userService.getById(userid).getPermission();
        if(permission<=2) {
            CardCustomer c = cardCustomerService.getOne(new LambdaQueryWrapper<CardCustomer>().eq(CardCustomer::getPackageId,id));
            BroadbandCustomer b = broadbandCustomerService.getOne(new LambdaQueryWrapper<BroadbandCustomer>().eq(BroadbandCustomer::getPackageId,id));
            if(b==null){
                if(c==null){
                    packageService.removeById(id);
                    return R.success("删除成功");
                }
                else return R.error(c.getName()+"客户正使用该套餐，无法删除");
            }
            else return R.error(b.getName()+"客户正使用该套餐，无法删除");
        }
        return R.error("权限不足");
    }
}
