package com.wei.broadband.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.UserAndRevenue;
import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.CardCustomer;
import com.wei.broadband.po.Revenue;
import com.wei.broadband.po.User;
import com.example.springbootmybatis.service.*;
import com.wei.broadband.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/revenue")
@Slf4j
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private BroadbandCustomerService broadbandCustomerService;

    @Autowired
    private CardCustomerService cardCustomerService;

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;

    @GetMapping("revenuelist")
    public R<List<Revenue>> GetRevenueList(HttpServletRequest request, int type ,String date1 , String date2){
        int id = (int) request.getSession().getAttribute("user");
        LambdaQueryWrapper<Revenue> queryWrapper = new LambdaQueryWrapper<>();
        if(date1!=null&&date1.length()>=1) queryWrapper.le(Revenue::getDate,date1);
        if(date2!=null&&date2.length()>=1) queryWrapper.ge(Revenue::getDate,date2);
        queryWrapper.eq(Revenue::getType,type).eq(Revenue::getUserId,id).orderByDesc(Revenue::getId);

        //话费补贴项
        if (type==4){
            queryWrapper.le(Revenue::getDate,LocalDate.now());
        }

        List<Revenue> revenueList = revenueService.list(queryWrapper);
        return R.success(revenueList);
    }

    @GetMapping("addrevenue")
    public R<String> AddRevenue(HttpServletRequest request,int userId, int type ,String date,String description, float amount){
        int id = (int) request.getSession().getAttribute("user");
        int p = userService.getById(id).getPermission();
        if(p>1) return R.error("权限不足");
        Revenue revenue = new Revenue();
        revenue.setUserId(userId);
        revenue.setDate(LocalDate.parse(date));
        revenue.setAmount(amount);
        revenue.setType(type);
        revenue.setDescription(description);

        if(type==0){
            LambdaQueryWrapper<Revenue> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Revenue::getType,0).eq(Revenue::getUserId,userId);
            revenueService.remove(queryWrapper);
        }
        revenueService.save(revenue);
        return R.success("操作成功");
    }

    @GetMapping("revenuelistpc")
    public R<List<UserAndRevenue>> GetRevenueListPc(HttpServletRequest request, int userId , String date1 , String date2){

        LambdaQueryWrapper<Revenue> queryWrapper = new LambdaQueryWrapper<>();
        if(date1!=null&&date1.length()>=1) queryWrapper.le(Revenue::getDate,date1);
        if(date2!=null&&date2.length()>=1) queryWrapper.ge(Revenue::getDate,date2);

        if(userService.getById(userId).getRevenueStatus()==1){
            queryWrapper.ne(Revenue::getType,0).ne(Revenue::getType,1);
        }

        queryWrapper.eq(Revenue::getUserId,userId).ne(Revenue::getType,0).orderByAsc(Revenue::getType);
        List<Revenue> revenueList = revenueService.list(queryWrapper);
        List<User> userList = userService.list();
        List<UserAndRevenue> userAndRevenueList = new ArrayList<>();

        LambdaQueryWrapper<Revenue> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Revenue::getType,0).eq(Revenue::getUserId,userId);



        if(userService.getById(userId).getRevenueStatus()==0){
            UserAndRevenue userAndRevenue = new UserAndRevenue();
            Revenue revenue = revenueService.getOne(queryWrapper1);
            userAndRevenue.setRevenue(revenue);
            if (revenue!=null){
                User user = userService.getById(revenue.getUserId());
                user.setPassword(null);
                userAndRevenue.setUser(user);
                userAndRevenueList.add(userAndRevenue);
            }
        }

        for (Revenue revenue :
                revenueList) {
            for (User user :
                    userList) {
                if(user.getId()==revenue.getUserId()){
                    UserAndRevenue userAndRevenue = new UserAndRevenue();
                    userAndRevenue.setRevenue(revenue);
                    userAndRevenue.setUser(user);
                    userAndRevenueList.add(userAndRevenue);
                    break;
                }
            }
        }
        return R.success(userAndRevenueList);
    }

    @DeleteMapping
    public R<String> delete(HttpServletRequest request,String ids){
        int userid = (int)request.getSession().getAttribute("user");
        int permission = userService.getById(userid).getPermission();
        String[] idlist = ids.split(",");
        if(permission<=1) {
            for (String idstring:
                    idlist) {
                int id = Integer.parseInt(idstring);
                revenueService.removeById(id);
            }

            return R.success("删除成功");
        }
        return R.error("权限不足");
    }

    @GetMapping("allcostomerrevenue")
    public R<String> AddAllCustomerRevenue(){
        List<BroadbandCustomer> broadbandCustomerList = broadbandCustomerService.list();
        List<CardCustomer> cardCustomerList = cardCustomerService.list();
        for (BroadbandCustomer broadbandCustomer :
                broadbandCustomerList) {
            Revenue revenue = new Revenue();
            revenue.setType(1);
            revenue.setDate(broadbandCustomer.getUpdateTime().toLocalDate());
            revenue.setAmount(packageService.getById(broadbandCustomer.getPackageId()).getCommission());
            revenue.setUserId(broadbandCustomer.getCreateUser());
            revenue.setDescription(broadbandCustomer.getName()+" "+packageService.getById(broadbandCustomer.getPackageId()).getName()+" 套餐提成");
            revenueService.save(revenue);
        }

        for (CardCustomer cardCustomer :
                cardCustomerList) {
            Revenue revenue = new Revenue();
            revenue.setType(1);
            revenue.setDate(cardCustomer.getUpdateTime().toLocalDate());
            revenue.setAmount(packageService.getById(cardCustomer.getPackageId()).getCommission());
            revenue.setUserId(cardCustomer.getCreateUser());
            revenue.setDescription(cardCustomer.getName()+" "+packageService.getById(cardCustomer.getPackageId()).getName()+" 套餐提成");
            revenueService.save(revenue);
        }
        return R.success("操作成功");
    }
}
