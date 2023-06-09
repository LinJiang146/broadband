package com.wei.broadband.dto;

import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.CustomerBilling;
import com.wei.broadband.po.Package;
import com.wei.broadband.po.User;
import lombok.Data;

import java.util.Map;

@Data
public class CusBillDTO {
    private String name;

    private String phone;

    private String packageName;

    private String broadbandNumber;

    private String creatUserName;

    private String status;

    private float phoneBills;

    private float balance;

    private float cost;

    private float arrears;

    private float grants;

    public CusBillDTO(BroadbandCustomer broadbandCustomer, CustomerBilling bill, Map<Integer, Package> packageMap , Map<Integer, User> userMap){
        name = broadbandCustomer.getName();
        phone = broadbandCustomer.getPhone();
        packageName = packageMap.get(broadbandCustomer.getPackageId()).getName();
        broadbandNumber = broadbandCustomer.getBroadbandNumber();
        creatUserName = userMap.get(broadbandCustomer.getCreateUser()).getName();
        status = bill.getStatus();
        phoneBills = bill.getPhoneBills();
        balance = bill.getBalance();
        cost = bill.getCost();
        arrears = bill.getArrears();
        grants = bill.getGrants();
    }
}
