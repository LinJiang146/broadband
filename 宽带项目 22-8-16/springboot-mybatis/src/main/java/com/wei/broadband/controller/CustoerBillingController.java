package com.wei.broadband.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.DataMap;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.CusBillDTO;

import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.CustomerBilling;
import com.wei.broadband.po.Package;
import com.wei.broadband.po.User;
import com.wei.broadband.service.BroadbandCustomerService;
import com.wei.broadband.service.CustomerBillingService;
import com.wei.broadband.service.PackageService;
import com.wei.broadband.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/bill")
@Slf4j
public class CustoerBillingController {



    @Autowired
    private CustomerBillingService billingService;



    @PostMapping("addBillList")
    public R<String> addBillList(@RequestBody List<CustomerBilling> billingList){

        return billingService.addBillList(billingList);




    }

    @GetMapping("getBillDate")
    public R<List<String>> getBillDate(){

        List<String> dateList = billingService.getBillDate();

        return R.success(dateList);
    }
    @GetMapping("getCusBillData")
    public R<List<CusBillDTO>> getCusBillData(String date){


        List<CusBillDTO> listDTO = billingService.getCusBillData(date);




        return R.success(listDTO);
    }
}
