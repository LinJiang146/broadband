//package com.example.springbootmybatis.dto;
//
//import com.example.springbootmybatis.common.R;
//import com.example.springbootmybatis.po.CustomerBilling;
//import lombok.Data;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//@Data
//public class CustomerBillingDTO {
//    private String phone;
//    private List<Billing> billingList = new ArrayList<>();
//    public static List<CustomerBillingDTO> toList(List<CustomerBilling> list){
//        List<CustomerBillingDTO> toList = new ArrayList<>();
//        HashMap<String, CustomerBillingDTO> map = new HashMap<>();
//        for (CustomerBilling b : list) {
//            if (map.get(b.getPhone())==null){
//                CustomerBillingDTO dto = new CustomerBillingDTO();
//                dto.setPhone(b.getPhone());
//                Billing billing1 = new Billing();
//                billing1.setDate(b.getDate());
//                billing1.setArrears(b.getArrears());
//                billing1.setBalance(b.getBalance());
//                billing1.setPhoneBills(b.getPhoneBills());
//                billing1.setCost(b.getCost());
//                billing1.setStatus(b.getStatus());
//                dto.getBillingList().add(billing1);
//                map.put(b.getPhone(),dto);
//                toList.add(dto);
//            }
//            else {
//                Billing billing1 = new Billing();
//                billing1.setDate(b.getDate());
//                billing1.setArrears(b.getArrears());
//                billing1.setBalance(b.getBalance());
//                billing1.setPhoneBills(b.getPhoneBills());
//                billing1.setCost(b.getCost());
//                billing1.setStatus(b.getStatus());
//                map.get(b.getPhone()).getBillingList().add(billing1);
//            }
//
//        }
//        return toList;
//    }
//}
//@Data
//class Billing{
//    private String date;
//    private String status;
//    private float phoneBills;
//    private float balance;
//    private float cost;
//    private float arrears;
//}
