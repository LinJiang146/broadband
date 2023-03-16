package com.example.springbootmybatis.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springbootmybatis.common.DataMap;
import com.example.springbootmybatis.common.R;
import com.example.springbootmybatis.dto.CusBillDTO;

import com.example.springbootmybatis.po.BroadbandCustomer;
import com.example.springbootmybatis.po.CustomerBilling;
import com.example.springbootmybatis.po.Package;
import com.example.springbootmybatis.po.User;
import com.example.springbootmybatis.service.BroadbandCustomerService;
import com.example.springbootmybatis.service.CustomerBillingService;
import com.example.springbootmybatis.service.PackageService;
import com.example.springbootmybatis.service.UserService;
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
    private BroadbandCustomerService broadbandCustomerService;

    @Autowired
    private CustomerBillingService billingService;

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;

    @Resource
    private DataMap dataMap;

    @PostMapping("addBillList")
    public R<String> addBillList(@RequestBody List<CustomerBilling> billingList){

        LambdaQueryWrapper<CustomerBilling> eq = new LambdaQueryWrapper<CustomerBilling>().eq(CustomerBilling::getDate, billingList.get(0).getDate());
        List<CustomerBilling> addedList = billingService.list(eq);
        Map<String, CustomerBilling> billmap = new HashMap<>();
        for (CustomerBilling customerBilling : addedList) {
            billmap.put(customerBilling.getPhone(),customerBilling);
        }

        List<CustomerBilling> list = new ArrayList<>();
        int n = 0;
        int m = 0;
        for (CustomerBilling b : billingList) {
            if (StringUtils.isNotEmpty(b.getPhone())&&StringUtils.isNotEmpty(b.getDate())){
                if (billmap.get(b.getPhone())==null){
                    list.add(b);
                    n++;
                    billmap.put(b.getPhone(),b);
                }else {
                    b.setId(billmap.get(b.getPhone()).getId());
                    list.add(b);
                    billmap.put(b.getPhone(),b);
                    m++;
                }
            }
        }
        billingService.saveOrUpdateBatch(list);

        return R.success("新增"+n+"条客户账单数据,修改"+m+"条同日期客户账单数据");
    }
//    @GetMapping("getBillList")
//    public R<List<CustomerBillingDTO>> getBillList(){
//        List<CustomerBilling> list = billingService.list();
//        List<CustomerBillingDTO> dtoList = CustomerBillingDTO.toList(list);
//        return R.success(dtoList);
//    }
    @GetMapping("getBillDate")
    public R<List<String>> getBillDate(){
        List<CustomerBilling> list = billingService.list();
        List<String> dateList = new ArrayList<>();
        dateList.add(list.get(0).getDate());
        for (CustomerBilling billing : list) {
            if (!dateList.get(dateList.size()-1).equals(billing.getDate())){
                dateList.add(billing.getDate());
            }
        }
        return R.success(dateList);
    }
    @GetMapping("getCusBillData")
    public R<List<CusBillDTO>> getCusBillData(String date){
        List<String> phoneList = new ArrayList<>();
        List<CusBillDTO> listDTO = new ArrayList<>();

        List<CustomerBilling> list = billingService.list(new LambdaQueryWrapper<CustomerBilling>().eq(CustomerBilling::getDate, date));
        //未找到该日期的数据时返回空列表
        if (list.size()==0) return R.success(null);

        for (CustomerBilling billing : list) {
            phoneList.add(billing.getPhone());
        }

        Map<Integer, User> id2UserMap = dataMap.getId2UserMap();

        Map<Integer,Package> id2PackageMap = dataMap.getId2PackageMap();

        LambdaQueryWrapper<BroadbandCustomer> customerLambdaQueryWrapper = new LambdaQueryWrapper<>();
        customerLambdaQueryWrapper.in(BroadbandCustomer::getPhone,phoneList);
        List<BroadbandCustomer> cusList = broadbandCustomerService.list(customerLambdaQueryWrapper);
        Map<String, BroadbandCustomer> cusMap = new HashMap<>();
        for (BroadbandCustomer broadbandCustomer : cusList) {
            cusMap.put(broadbandCustomer.getPhone(),broadbandCustomer);
        }
        for (CustomerBilling billing : list) {
            if (cusMap.get(billing.getPhone())!=null){
                CusBillDTO DTO = new CusBillDTO(cusMap.get(billing.getPhone()), billing, id2PackageMap, id2UserMap);
                listDTO.add(DTO);
            }
        }

        return R.success(listDTO);
    }
}
