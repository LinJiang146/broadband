package com.wei.broadband.common;

import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.CustomerBilling;
import com.wei.broadband.po.Package;
import com.wei.broadband.po.User;
import com.example.springbootmybatis.service.*;
import com.wei.broadband.service.*;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class DataMap implements InitializingBean {

    @Resource
    private BroadbandCustomerService broadbandCustomerService;
    @Resource
    private CommissionService commissionService;
    @Resource
    private MaintainService maintainService;
    @Resource
    private PackageService packageService;
    @Resource
    private RevenueService revenueService;
    @Resource
    private TextService textService;
    @Resource
    private UserService userService;
    @Resource
    private CustomerBillingService billingService;

    private Map<Integer, BroadbandCustomer> id2CusMap = new HashMap<>();

    private Map<String,BroadbandCustomer> phone2CusMap = new HashMap<>();

    private Map<String, BroadbandCustomer> broadNumber2CusMap = new HashMap<>();

    private Map<Integer, Package> id2PackageMap = new HashMap<>();

    private Map<String, CustomerBilling> phoneDate2BillMap = new HashMap<>();

    private Map<Integer, User> id2UserMap = new HashMap<>();

    private Map<String, User> name2UserMap = new HashMap<>();

    private Map<String, Package> name2PackageMap = new HashMap<>();

    //用AOP进行数据刷新



    @Override
    public void afterPropertiesSet() throws Exception {
        List<BroadbandCustomer> cusList = broadbandCustomerService.list();
        for (BroadbandCustomer broadbandCustomer : cusList) {
            phone2CusMap.put(broadbandCustomer.getPhone(),broadbandCustomer);
            if (StringUtils.isNotEmpty(broadbandCustomer.getBroadbandNumber())){
                broadNumber2CusMap.put(broadbandCustomer.getBroadbandNumber(), broadbandCustomer);
            }
            id2CusMap.put(broadbandCustomer.getId(),broadbandCustomer);
        }
        List<Package> packageList = packageService.list();
        for (Package aPackage : packageList) {
            id2PackageMap.put(aPackage.getId(),aPackage);
            name2PackageMap.put(aPackage.getName(), aPackage);
        }
        List<CustomerBilling> billingList = billingService.list();
        for (CustomerBilling billing : billingList) {
            phoneDate2BillMap.put(billing.getPhone()+billing.getDate(),billing);
        }
        List<User> userList = userService.list();
        for (User user : userList) {
            id2UserMap.put(user.getId(),user);
            name2UserMap.put(user.getName(), user);
        }
    }

}
