package com.wei.broadband.dto;

import com.wei.broadband.common.DataMap;
import com.example.springbootmybatis.po.*;
import com.wei.broadband.po.Package;
import com.example.springbootmybatis.service.*;
import com.wei.broadband.po.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DTO implements InitializingBean{


    @Resource
    private ModelMapper modelMapper;

    @Resource
    private DataMap dataMap;

    //可调优减少数据库访问

    private Map<Integer, BroadbandCustomer> id2CusMap;

    private Map<String, BroadbandCustomer> phone2CusMap;

    private Map<Integer, Package> id2PackageMap;

    private Map<String, CustomerBilling> phoneDate2BillMap;

    private Map<Integer, User> id2UserMap;

    public BroadbandDTO broadbandDTO(BroadbandCustomer broadbandCustomer){

        BroadbandDTO map = modelMapper.map(broadbandCustomer, BroadbandDTO.class);
        map.setPackageName(id2PackageMap.get(broadbandCustomer.getPackageId()).getName());
        String[] status = {"","开通","欠费停机","高额停机","已销户"};
        if (broadbandCustomer.getStatus()<5&&broadbandCustomer.getStatus()>0)
        map.setStatus(status[broadbandCustomer.getStatus()]);
        map.setCreateUser(id2UserMap.get(broadbandCustomer.getCreateUser()).getName());
        return map;
    }

    public List<BroadbandDTO> broadbandDTO(List<BroadbandCustomer> broadbandCustomerList){
        List<BroadbandDTO> list = new ArrayList<>();
        for (BroadbandCustomer broadbandCustomer : broadbandCustomerList) {
            list.add(broadbandDTO(broadbandCustomer));
        }
        return list;
    }

    public CommissionDTO commissionDTO(Commission commission){
        CommissionDTO dto = new CommissionDTO();

        BroadbandCustomer cus = phone2CusMap.get(commission.getPhone());
        Package aPackage = id2PackageMap.get(cus.getPackageId());
        dto.setName(cus.getName());
        dto.setPhone(cus.getPhone());
        dto.setAmount1(commission.getAmount1());
        dto.setAmount2(commission.getAmount2());
        dto.setAmount3(commission.getAmount3());
        dto.setBroadNumber(commission.getBroadbandNumber());
        dto.setPackageName(aPackage.getName());
        dto.setPolicy(commission.getPolicy());
        CustomerBilling billing = phoneDate2BillMap.get(commission.getPhone()+commission.getDate());
        if (billing!=null){
            dto.setPhoneBills(billing.getPhoneBills());
            dto.setStatus(billing.getStatus());
            dto.setGrants(billing.getGrants());
        }
        return dto;
    }

    public List<CommissionDTO> commissionDTO(List<Commission> commissionList){
        List<CommissionDTO> list = new ArrayList<>();
        for (Commission commission : commissionList) {
            list.add(commissionDTO(commission));
        }
        return list;
    }

    public MissionDTO missionDTO(MissionEntity mission){
        MissionDTO missionDTO = modelMapper.map(mission,MissionDTO.class);
        if (mission.getUserId()!=null&&mission.getUserId()!=0){
            missionDTO.setUserName(id2UserMap.get(mission.getUserId()).getName());
        }
        if (mission.getCustomerId()!=null&&mission.getCustomerId()!=0){
            BroadbandCustomer broadbandCustomer = id2CusMap.get(mission.getCustomerId());
            missionDTO.setCustomerName(broadbandCustomer.getName());
            missionDTO.setPhone(broadbandCustomer.getPhone());
            missionDTO.setLat(broadbandCustomer.getLat());
            missionDTO.setLng(broadbandCustomer.getLng());
            missionDTO.setAddress(broadbandCustomer.getAddress());
        }
        return missionDTO;
    }

    public List<MissionDTO> missionDTO(List<MissionEntity> missionList){
        List<MissionDTO> missionDTOList = new ArrayList<>();
        for (MissionEntity missionEntity : missionList) {
            missionDTOList.add(missionDTO(missionEntity));
        }
        return missionDTOList;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        phone2CusMap = dataMap.getPhone2CusMap();

        id2CusMap = dataMap.getId2CusMap();

        id2PackageMap = dataMap.getId2PackageMap();

        phoneDate2BillMap = dataMap.getPhoneDate2BillMap();

        id2UserMap = dataMap.getId2UserMap();
    }
}
