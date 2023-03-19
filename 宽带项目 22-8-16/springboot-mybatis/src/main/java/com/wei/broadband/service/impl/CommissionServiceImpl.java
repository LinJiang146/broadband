package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.broadband.common.DataMap;
import com.wei.broadband.common.ExcelHandle;
import com.wei.broadband.common.R;
import com.wei.broadband.common.StringHandle;
import com.wei.broadband.dto.DTO;
import com.wei.broadband.mapper.CommissionMapper;
import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.Commission;
import com.wei.broadband.po.Package;
import com.wei.broadband.po.User;
import com.wei.broadband.po.excelPo.InExcel;
import com.wei.broadband.po.excelPo.PortFeesExcel;
import com.wei.broadband.service.CommissionService;
import com.wei.broadband.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class CommissionServiceImpl extends ServiceImpl<CommissionMapper, Commission> implements CommissionService {

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private DTO dto;

    @Resource
    private DataMap dataMap;


    @Override
    public List<User> getFcUser(int id) {
        List<User> userList = userService.list();
        List<User> userList1 = new ArrayList<>();
        List<User> list = new ArrayList<>();

        for (User u :
                userList) {
            if (u.getCreateUser()==id) userList1.add(u);
        }

        for (User u :
                userList) {
            for (int i = 0;i<userList1.size();i++) {
                User user = userList1.get(i);
                if (u.getCreateUser()==user.getId()&&u.getId()!=user.getId()) {
                    userList1.add(u);
                    break;
                }
            }
        }
        for (User u :
                userList) {
            for (int i = 0;i<userList1.size();i++) {
                User user = userList1.get(i);
                if (u.getCreateUser()==user.getId()&&u.getId()!=user.getId()) {
                    userList1.add(u);
                    break;
                }
            }
        }
        for (User user :
                userList1) {
            if (list.size() == 0) list.add(user);
            else for (int i = 0;i<list.size();i++) {
                if (list.get(i).getId()==user.getId()||user.getId()==1) break;
                if (i== list.size()-1) {
                    user.setPassword(null);
                    list.add(user);
                }
            }
        }
        return list;
    }

    @Override
    public List<Commission> getList(int id, String date1, String date2, int type) {
        StringBuffer stringBuilder1 = new StringBuffer(date1);
        stringBuilder1.delete(4,5);
        stringBuilder1.delete(6,9);
        String d1 = stringBuilder1.toString();
        StringBuffer stringBuilder2 = new StringBuffer(date2);
        stringBuilder2.delete(4,5);
        stringBuilder2.delete(6,9);
        String d2 = stringBuilder2.toString();

        LambdaQueryWrapper<Commission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(Commission::getDate,d1).ge(Commission::getDate,d2).eq(Commission::getCreatUserId,id).eq(Commission::getType,type);
        List<Commission> list = commissionService.list(queryWrapper);
        return list;
    }

    @Override
    public R<String> getPortFeesExcel(MultipartFile file) throws IOException {
        int n = 0;

        List<Commission> commissionList = commissionService.list();
        Map<String, Commission> broadDate2Commission = new HashMap<>();
        for (Commission commission : commissionList) {
            //以号码加日期为唯一表示
            broadDate2Commission.put(commission.getBroadbandNumber() + commission.getDate(), commission);
        }
        List<PortFeesExcel> list = ExcelHandle.getListFormExcel(file, PortFeesExcel.class);

        //设置端口费
        for (PortFeesExcel portFeesExcel : list) {
            String broadNumber = StringHandle.broadNumber(portFeesExcel.getBroadNumber());
            String date = StringHandle.insertString(portFeesExcel.getDate(), '-', 4);
            Commission commission = broadDate2Commission.get(broadNumber + date);
            if (commission!=null&&portFeesExcel.getPortFee()!=0.0){
                commission.setAmount3(portFeesExcel.getPortFee());

                n++;
            }
        }
        HashSet<Commission> set = new HashSet<>(broadDate2Commission.values());
        commissionService.saveOrUpdateBatch(set);
        return R.success("添加或更新了" + n + "条端口费数据");
    }

    @Override
    public R<String> getCommissionExcel(MultipartFile file) throws IOException {
        List<Commission> commissionList = commissionService.list();
        Map<String, Commission> fcDetailsMap = new HashMap<>();
        for (Commission commission : commissionList) {
            //以号码加日期为唯一表示
            fcDetailsMap.put(commission.getPhone() + commission.getDate(), commission);
        }


        Map<String, BroadbandCustomer> customerMap = dataMap.getPhone2CusMap();
        Map<String, BroadbandCustomer> customerMapByBroadNumber = dataMap.getBroadNumber2CusMap();
        Map<Integer, User> userMap = dataMap.getId2UserMap();
        Map<Integer, Package> packageMap = dataMap.getId2PackageMap();

        List<InExcel> list = ExcelHandle.getListFormExcel(file,InExcel.class);


        for (InExcel inExcel : list) {
            inExcel.setDate(StringHandle.insertString(inExcel.getDate(),'-',4));
            Commission fc = new Commission();
            fc.setPolicy(inExcel.getPolicyName());
            if (inExcel.getPolicyName().contains("合伙人")) {

                //处理手机号码
                Pattern r = Pattern.compile("^1[0-9]{10}$");
                if (r.matcher(inExcel.getFazhanNumber()).matches()) {
                    Commission commission = fcDetailsMap.get(inExcel.getFazhanNumber() + inExcel.getDate());
                    if (commission == null) {
                        BroadbandCustomer cus = customerMap.get(inExcel.getFazhanNumber());
                        if (cus == null) continue;
                        fc.setAmount1(inExcel.getAmount());
                        fc.setPhone(cus.getPhone());


                        fc.setDate(inExcel.getDate());
                        fc.setBroadbandNumber(cus.getBroadbandNumber());
                        fcDetailsMap.put(cus.getPhone() + inExcel.getDate(), fc);
                    } else {
                        commission.setAmount1(inExcel.getAmount());


                        fc.setDate(inExcel.getDate());

                        commission.setAmount(commission.getAmount1() + commission.getAmount2());
                    }
                }
                //处理宽带号码
                r = Pattern.compile("^[0]*775[0-9]*");
                if (r.matcher(inExcel.getFazhanNumber()).matches()) {
                    //宽带号补0
                    inExcel.setFazhanNumber(StringHandle.broadNumber(inExcel.getFazhanNumber()));


                    BroadbandCustomer cus = customerMapByBroadNumber.get(inExcel.getFazhanNumber());
                    if (cus == null) continue;
                    String phone = cus.getPhone();
                    Commission commission = fcDetailsMap.get(phone + inExcel.getDate());
                    if (commission == null) {

                        fc.setAmount2(inExcel.getAmount());
                        fc.setBroadbandNumber(cus.getBroadbandNumber());
                        fcDetailsMap.put(cus.getPhone() + inExcel.getDate(), fc);
                    } else {
                        commission.setBroadbandNumber(cus.getBroadbandNumber());
                        commission.setAmount2(inExcel.getAmount());
                        commission.setAmount(commission.getAmount1() + commission.getAmount2());
                    }

                }

            } else if (inExcel.getPolicyName().contains("星计划")) {

                Pattern r = Pattern.compile("^1[0-9]{10}$");
                if (r.matcher(inExcel.getFazhanNumber()).matches()) {
                    BroadbandCustomer cus = customerMap.get(inExcel.getFazhanNumber());
                    if (cus == null) continue;
                    Commission commission = fcDetailsMap.get(inExcel.getFazhanNumber() + inExcel.getDate());

                    if (commission == null) {

                        fc.setPhone(cus.getPhone());
                        fc.setAmount1(inExcel.getAmount());
                        fc.setAmount2(0);
                        fc.setAmount(inExcel.getAmount());
                        fc.setDate(inExcel.getDate());
                        fcDetailsMap.put(cus.getPhone() + inExcel.getDate(), fc);
                    }
                    else {
                        //有相同日期客户信息进行覆盖
                        commission.setPhone(inExcel.getFazhanNumber());
                        commission.setAmount1(inExcel.getAmount());
                        commission.setAmount2(0);
                        commission.setAmount(inExcel.getAmount());
                        commission.setDate(inExcel.getDate());
                    }
                }
            }
        }
        HashSet<Commission> set = new HashSet<>(fcDetailsMap.values());
        for (Commission commission : fcDetailsMap.values()) {
            if (commission!=null&&!StringUtils.isNotEmpty(commission.getPhone())){
                set.remove(commission);
            }
        }
        commissionService.saveOrUpdateBatch(set);
        return R.success("新增" + (set.size() - commissionList.size()) + "条佣金明细数据");
    }

    @Override
    public List<String> getFcDate() {
        List<Commission> list = commissionService.list();
        List<String> dateList = new ArrayList<>();
        dateList.add(list.get(0).getDate());
        for (Commission commission : list) {
            if (!dateList.get(dateList.size()-1).equals(commission.getDate())){
                dateList.add(commission.getDate());
            }
        }
        Collections.sort(dateList);
        return dateList;
    }
}
