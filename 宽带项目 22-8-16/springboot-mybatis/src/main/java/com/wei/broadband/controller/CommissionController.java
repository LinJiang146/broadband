package com.wei.broadband.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.DataMap;
import com.wei.broadband.common.ExcelHandle;
import com.wei.broadband.common.R;
import com.wei.broadband.common.StringHandle;
import com.wei.broadband.dto.CommissionDTO;
import com.wei.broadband.dto.DTO;
import com.wei.broadband.po.Package;
import com.wei.broadband.po.excelPo.InExcel;
import com.wei.broadband.po.excelPo.PortFeesExcel;
import com.wei.broadband.service.CommissionService;
import com.wei.broadband.service.UserService;
import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.Commission;
import com.wei.broadband.po.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
@RequestMapping("/commission")
@Slf4j
public class CommissionController {

    @Autowired
    private CommissionService commissionService;



    @Autowired
    private DTO dto;


    @GetMapping("getFcDate")
    public R<List<String>> getFcDate(){

        List<String> dateList = commissionService.getFcDate();


        return R.success(dateList);
    }
    @GetMapping("getCommissionData")
    public R<List<CommissionDTO>> getCommissionData(String date){
        List<Commission> list = commissionService.list(new LambdaQueryWrapper<Commission>().eq(Commission::getDate, date));
        return R.success(dto.commissionDTO(list));
    }

    @PostMapping("/commissionExcel")
    public R<String> getCommissionExcel(MultipartFile file) throws IOException {

        return commissionService.getCommissionExcel(file);

    }
    @PostMapping("/portFeesExcel")
    public R<String> getPortFeesExcel(MultipartFile file) throws IOException {
        return commissionService.getPortFeesExcel(file);
    }



    @GetMapping("/list")
    public R<List<Commission>> getList(int id, String date1, String date2, int type){
        List<Commission> list = commissionService.getList(id,date1,date2,type);
        return R.success(list);
    }

    @GetMapping("/text")
    public R<List<String>> gettext(){

        return R.success(null);
    }

    @GetMapping("fcuser")
    public R<List<User>> getFcUser(int id){

        List<User> list = commissionService.getFcUser(id);



        return R.success(list);
    }
}
