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


    @PostMapping("/add")
    public R<String> save(HttpServletRequest request, @RequestBody Package pack) {
        int id = (int) request.getSession().getAttribute("user");
        return packageService.savePackage(id,pack);

    }

    @GetMapping("/lists")
    public R<List<Package>> list(String content,Integer sort,Integer type){

        return packageService.packageList(content,sort,type);



    }


    @GetMapping("/getbyid")
    public R<Package> getById(int id){
        Package pack = packageService.getById(id);
        return R.success(pack);
    }

    @PostMapping("/updata")
    public R<String> updata(HttpServletRequest request,@RequestBody Package pack){
        int id = (int)request.getSession().getAttribute("user");
        return packageService.updataPackage(id,pack);

    }


    @DeleteMapping
    public R<String> delete(HttpServletRequest request,int id){
        int userid = (int)request.getSession().getAttribute("user");
        return packageService.deletePackage(userid,id);


    }
}
