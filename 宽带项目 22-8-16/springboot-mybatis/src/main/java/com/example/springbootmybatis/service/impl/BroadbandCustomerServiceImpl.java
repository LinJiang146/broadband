package com.example.springbootmybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmybatis.common.R;
import com.example.springbootmybatis.dto.BroadbandDTO;
import com.example.springbootmybatis.dto.DTO;
import com.example.springbootmybatis.mapper.BroadbandCustomerMapper;
import com.example.springbootmybatis.po.BroadbandCustomer;
import com.example.springbootmybatis.po.Revenue;
import com.example.springbootmybatis.po.User;
import com.example.springbootmybatis.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Service
public class BroadbandCustomerServiceImpl extends ServiceImpl<BroadbandCustomerMapper, BroadbandCustomer> implements BroadbandCustomerService {

    @Autowired
    private DTO dto;

    @Autowired
    private UserService userService;


    @Autowired
    private BroadbandCustomerService broadbandCustomerService;



    @Autowired
    private PackageService packageService;

    @Autowired
    private MaintainService maintainService;

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private CustomerBillingService billingService;


    @Override
    public List<BroadbandDTO> getMyCusList(int id) {
        LambdaQueryWrapper<BroadbandCustomer> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(BroadbandCustomer::getCreateUser,id).orderByDesc(BroadbandCustomer::getId);
        List<BroadbandCustomer> list = list(queryWrapper);
        List<BroadbandDTO> dtoList = dto.broadbandDTO(list);
        return dtoList;
    }

    @Override
    public List<BroadbandCustomer> cusList(HttpServletRequest request, String content, Integer sort, Integer select, Integer current, Integer size) {
        LambdaQueryWrapper<BroadbandCustomer> queryWrapper = new LambdaQueryWrapper<>();

        //权限不足时只显示自己创建的客户
        int id = (int)request.getSession().getAttribute("user");
        int permission = userService.getById(id).getPermission();
        if(permission==4) {
            queryWrapper.eq(BroadbandCustomer::getCreateUser,id);
        }
        //content相关筛选
        if(StringUtils.isNotEmpty(content)) {
            User createUser = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getName,content));
            int createId = 0;
            if (createUser!=null) createId = createUser.getId();
            int finalCreateId = createId;
            queryWrapper.and((i)->{i.like(BroadbandCustomer::getName, content).or().
                    like(BroadbandCustomer::getAddress, content).or().
                    like(BroadbandCustomer::getPhone, content).or().
                    like(BroadbandCustomer::getAlternatePhone, content).or().
                    eq(createUser!=null,BroadbandCustomer::getCreateUser, finalCreateId);
            });
        }
        //查询类型

        if (select>=1&&select<=4){
            queryWrapper.eq(BroadbandCustomer::getStatus,select);
        }


        //排序
        if(sort==0)
            queryWrapper.orderByDesc(BroadbandCustomer::getUpdateTime);
        if(sort==1)
            queryWrapper.orderByAsc(BroadbandCustomer::getUpdateTime);



        List<BroadbandCustomer> broadbandCustomers = null;
        if (current!=null&&size!=null){
            Page<BroadbandCustomer> page = new Page<>(current,size);
            Page<BroadbandCustomer> page1 = page(page, queryWrapper);
            broadbandCustomers = page1.getRecords();
        }else {
            broadbandCustomers = list(queryWrapper);
        }
        return broadbandCustomers;
    }

    @Override
    public int getCusCount(String content, int sort, int select) {
        LambdaQueryWrapper<BroadbandCustomer> queryWrapper = new LambdaQueryWrapper<>();
        //权限不足时只显示自己创建的客户
        //content相关筛选
        if(StringUtils.isNotEmpty(content)) {
            User createUser = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getName,content));
            int createId = 0;
            if (createUser!=null) createId = createUser.getId();
            int finalCreateId = createId;
            queryWrapper.and((i)->{i.like(BroadbandCustomer::getName, content).or().
                    like(BroadbandCustomer::getAddress, content).or().
                    like(BroadbandCustomer::getPhone, content).or().
                    like(BroadbandCustomer::getAlternatePhone, content).or().
                    eq(createUser!=null,BroadbandCustomer::getCreateUser, finalCreateId);
            });
        }
        //查询类型

        if (select>=1&&select<=4){
            queryWrapper.eq(BroadbandCustomer::getStatus,select);
        }

        //排序
        if(sort==0)
            queryWrapper.orderByDesc(BroadbandCustomer::getUpdateTime);
        if(sort==1)
            queryWrapper.orderByAsc(BroadbandCustomer::getUpdateTime);

        int count = count(queryWrapper);
        return count;
    }

    @Override
    public R<String> addCus(int id, BroadbandCustomer broadbandCustomer) {
        LambdaQueryWrapper<BroadbandCustomer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BroadbandCustomer::getPhone,broadbandCustomer.getPhone())
                .eq(BroadbandCustomer::getName,broadbandCustomer.getName());
        BroadbandCustomer b = broadbandCustomerService.getOne(queryWrapper);
        if (b!=null) R.error("已存在同名同号码的客户");


        broadbandCustomer.setCreateUser(id);
        broadbandCustomer.setUpdateUser(id);
        if (StringUtils.isNotEmpty(broadbandCustomer.getBroadbandNumber())&&!broadbandCustomer.getBroadbandNumber().substring(0,1).equals("0")){
            StringBuffer insert = new StringBuffer(broadbandCustomer.getBroadbandNumber()).insert(0, "0");
            broadbandCustomer.setBroadbandNumber(insert.toString());
        }

        broadbandCustomerService.save(broadbandCustomer);



        Revenue revenue = new Revenue();
        revenue.setType(1);
        revenue.setDate(LocalDate.now());
        revenue.setAmount(packageService.getById(broadbandCustomer.getPackageId()).getCommission());
        revenue.setUserId(broadbandCustomer.getCreateUser());
        revenue.setDescription(broadbandCustomer.getName()+" "+packageService.getById(broadbandCustomer.getPackageId()).getName()+" 套餐提成");
        revenueService.save(revenue);



        return R.success("新增客户成功");
    }

    @Override
    public BroadbandCustomer getByPhone(String phone) {
        LambdaQueryWrapper<BroadbandCustomer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BroadbandCustomer::getPhone,phone);
        BroadbandCustomer customer = broadbandCustomerService.getOne(queryWrapper);
        return  customer;
    }

    @Override
    public R<String> deleteCus(int userid, int id) {

        int permission = userService.getById(userid).getPermission();
        if(permission<=2) {
            broadbandCustomerService.removeById(id);
            return R.success("删除成功");
        }
        return R.error("权限不足");
    }

    @Override
    public R<String> updateCus(int id, BroadbandCustomer broadbandCustomer) {
        LambdaQueryWrapper<BroadbandCustomer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BroadbandCustomer::getPhone,broadbandCustomer.getPhone())
                .eq(BroadbandCustomer::getName,broadbandCustomer.getName());
        BroadbandCustomer b = broadbandCustomerService.getOne(queryWrapper);
        if (b!=null) return R.error("已存在同名同号码的客户");



        int permission = userService.getById(id).getPermission();
        if(permission<=2) {
            broadbandCustomer.setUpdateUser(id);
            if (StringUtils.isNotEmpty(broadbandCustomer.getBroadbandNumber())&&!broadbandCustomer.getBroadbandNumber().substring(0,1).equals("0")){
                StringBuffer insert = new StringBuffer(broadbandCustomer.getBroadbandNumber()).insert(0, "0");
                broadbandCustomer.setBroadbandNumber(insert.toString());
            }
            broadbandCustomerService.updateById(broadbandCustomer);
            return R.success("更新成功");
        }
        return R.error("权限不足");
    }

    @Override
    public List<BroadbandCustomer> broadbandList() {
        LambdaQueryWrapper<BroadbandCustomer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(BroadbandCustomer::getBroadbandNumber);
        List<BroadbandCustomer> list = broadbandCustomerService.list(queryWrapper);
        return list;
    }

    @Override
    public void addBroadband(int id, String number) {

        BroadbandCustomer broadbandCustomer = broadbandCustomerService.getById(id);
        broadbandCustomer.setBroadbandNumber(number);
        broadbandCustomerService.updateById(broadbandCustomer);
    }
}
