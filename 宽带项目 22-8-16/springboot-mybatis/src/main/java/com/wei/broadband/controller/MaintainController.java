package com.wei.broadband.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.BroadbandAndMaintain;
import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.Maintain;
import com.wei.broadband.po.Package;
import com.wei.broadband.po.User;
import com.wei.broadband.service.BroadbandCustomerService;
import com.wei.broadband.service.MaintainService;
import com.wei.broadband.service.PackageService;
import com.wei.broadband.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/maintain")
@Slf4j
public class MaintainController {
    @Autowired
    private BroadbandCustomerService broadbandCustomerService;

    @Autowired
    private UserService userService;

    @Autowired
    private MaintainService maintainService;

    @Autowired
    private PackageService packageService;

    @GetMapping("/maintainlist")
    public R<List<BroadbandCustomer>> MaintainList(HttpServletRequest request, int select , String date1 , String date2){
        int id = (int)request.getSession().getAttribute("user");
        LambdaQueryWrapper<Maintain> queryWrapper = new LambdaQueryWrapper<>();
        if(date1!=null&&date1.length()>=1) queryWrapper.le(Maintain::getDate,date1);
        if (date2!=null&&date2.length()>=1) queryWrapper .ge(Maintain::getDate,date2);
        queryWrapper.eq(Maintain::getUserId,id);

        if(select == 0){
            queryWrapper.isNull(Maintain::getImage);
        }
        else {
            queryWrapper.isNotNull(Maintain::getImage);
        }
        queryWrapper.orderByDesc(Maintain::getId);
        List<Maintain> list = maintainService.list(queryWrapper);
        List<BroadbandCustomer> broadbandCustomerList = new ArrayList<>();

        for (int i = 0 ; i< list.size();i++){

            BroadbandCustomer b = broadbandCustomerService.getById(list.get(i).getCustomerId());
            if(b==null) continue;
            b.setCreateTime(list.get(i).getDate().atTime(0,0,0));
            b.setUpdateTime(list.get(i).getDate().plusWeeks(1).atTime(0,0,0));
            b.setCreateUser(list.get(i).getUserId());
            b.setUpdateUser(list.get(i).getId());
            broadbandCustomerList.add(b);
        }

        return R.success(broadbandCustomerList);
    }
    @GetMapping("/allmaintainlist")
    public R<List<BroadbandCustomer>> AllMaintainList(HttpServletRequest request, int select ,String date1 , String date2,String content){

        LambdaQueryWrapper<Maintain> queryWrapper = new LambdaQueryWrapper<>();
        if(date1!=null&&date1.length()>=1) queryWrapper.le(Maintain::getDate,date1);
        if (date2!=null&&date2.length()>=1) queryWrapper.ge(Maintain::getDate,date2);
        queryWrapper.isNotNull(Maintain::getDate);
        if(select == 0){
            queryWrapper.isNull(Maintain::getImage);
        }
        else {
            queryWrapper.isNotNull(Maintain::getImage);
        }
        queryWrapper.orderByDesc(Maintain::getId);
        List<Maintain> list = maintainService.list(queryWrapper);
        List<BroadbandCustomer> broadbandCustomerList = new ArrayList<>();
        for (int i = 0 ; i< list.size();i++){

            BroadbandCustomer b = broadbandCustomerService.getById(list.get(i).getCustomerId());
            if(b==null) continue;
            b.setCreateTime(list.get(i).getDate().atTime(0,0,0));
            b.setUpdateTime(list.get(i).getDate().plusWeeks(1).atTime(0,0,0));
            b.setCreateUser(list.get(i).getUserId());
            b.setUpdateUser(list.get(i).getId());
            broadbandCustomerList.add(b);
        }

        List<User> userList = userService.list();
        List<Package> packageList = packageService.list();
        List<BroadbandCustomer> r = new ArrayList<>();

        if(content!=null&&content.length()>=1){

            for (BroadbandCustomer b :
                    broadbandCustomerList) {
                if(b.getName().contains(content)||b.getPhone().contains(content)
                        ||(b.getAlternatePhone()!=null&&b.getAlternatePhone().contains(content))
                        ||b.getAddress().contains(content)
                        ||IsContains(content,b.getCreateUser(),b.getPackageId(),userList,packageList)){

                    r.add(b);
                }
            }
            return R.success(r);
        }else return R.success(broadbandCustomerList);
    }
    @GetMapping("/allbroadbandandmaintain")
    public R<List<BroadbandAndMaintain>> MaintainAndBroadbandList(HttpServletRequest request ,int select,  String content,String date1,String date2){

        LambdaQueryWrapper<Maintain> queryWrapper = new LambdaQueryWrapper<>();
        if(select == 1) queryWrapper.isNull(Maintain::getImage);
        if(select == 2) queryWrapper.isNotNull(Maintain::getImage);
        if(date1!=null&&date1.length()>=1) queryWrapper.isNotNull(Maintain::getDate).le(Maintain::getDate,date1);
        if(date2!=null&&date2.length()>=1) queryWrapper.isNotNull(Maintain::getDate).ge(Maintain::getDate,date2);
        queryWrapper.orderByDesc(Maintain::getId);
        List<Maintain> list = maintainService.list(queryWrapper);
        List<BroadbandAndMaintain> broadbandAndMaintainList = new ArrayList<>();

        for (int i = 0 ; i< list.size();i++){

            BroadbandCustomer b = broadbandCustomerService.getById(list.get(i).getCustomerId());
            if(b==null) continue;

            BroadbandAndMaintain broadbandAndMaintain = new BroadbandAndMaintain();
            broadbandAndMaintain.setMaintain(list.get(i));
            broadbandAndMaintain.setBroadbandCustomer(b);
            broadbandAndMaintainList.add(broadbandAndMaintain);
        }

        List<BroadbandAndMaintain> r = new ArrayList<>();

        if(content!=null&&content.length()>=1){
            List<User> userList = userService.list();
            List<Package> packageList = packageService.list();
            for (BroadbandAndMaintain bm :
                    broadbandAndMaintainList) {
                BroadbandCustomer b =bm.getBroadbandCustomer();
                if(b.getName().contains(content)||b.getPhone().contains(content)
                        ||(b.getAlternatePhone()!=null&&b.getAlternatePhone().contains(content))
                        ||b.getAddress().contains(content)
                        ||IsContains(content,bm.getMaintain().getUserId(), b.getPackageId(),userList,packageList)){

                    r.add(bm);
                }
            }
            return R.success(r);
        }else
        return R.success(broadbandAndMaintainList);
    }


    private boolean IsContains(String content,int userId,int packageId, List<User> userList , List<Package> packageList){
        for (User u :
                userList) {
            if (u.getId()==userId&&u.getName().contains(content)){
                return true;
            }
        }
        for (Package p :
                packageList) {
            if (p.getId()==packageId&&p.getName().contains(content)){
                return true;
            }
        }
        return false;
    }

    @GetMapping("/updatemaintain")
    public R<String> UpdateMaintain(HttpServletRequest request,int id , String image,int satisfaction ,String warning){
        Maintain maintain = maintainService.getById(id);
        maintain.setImage(image);
//        maintain.setSatisfaction(satisfaction);
//        maintain.setWarning(warning);
        maintainService.updateById(maintain);
        return R.success("完成工单");
    }

    @GetMapping("/updateuser")
    public R<String> Updateuser(HttpServletRequest request,int id ,int userid){
        Maintain maintain = maintainService.getById(id);
        maintain.setUserId(userid);
        maintainService.updateById(maintain);
        return R.success("完成修改");
    }

    @PostMapping("/updatemaintainpc")
    public R<String> UpdateMaintainPc(@RequestBody BroadbandAndMaintain broadbandAndMaintain){
        Maintain maintain = broadbandAndMaintain.getMaintain();
//        System.out.println(maintain.getId()+" "+maintain.getBalance());
        maintainService.updateById(maintain);
        return R.success("保存成功");
    }

    @PostMapping("/updatemaintainpcall")
    public R<String> UpdateMaintainPcAll(@RequestBody List<BroadbandAndMaintain> broadbandAndMaintainList){
        for (BroadbandAndMaintain broadbandAndMaintain :
                broadbandAndMaintainList) {
            maintainService.updateById(broadbandAndMaintain.getMaintain());
        }
        return R.success("保存成功");
    }

    @GetMapping("/updateusers")
    public R<String> Updateusers(HttpServletRequest request,String ids ,int userid){
        String[] idlist = ids.split(",");
        for (String idstring :
                idlist) {
            int id = Integer.parseInt(idstring);
            Maintain maintain = maintainService.getById(id);
            maintain.setUserId(userid);
            maintainService.updateById(maintain);
        }
        return R.success("完成修改");
    }

    @GetMapping("/allBroadbandmaintain")
    public R<String> AllMaintain(){

        List<BroadbandCustomer> broadbandCustomerList = broadbandCustomerService.list();
        List<Maintain> maintainList = new ArrayList<>();
        for (BroadbandCustomer b :
                broadbandCustomerList) {
            Maintain m = new Maintain();
            m.setCustomerId(b.getId());
            m.setUserId(b.getCreateUser());
            maintainList.add(m);
        }
        maintainService.saveBatch(maintainList);
        return R.success("完成工单");
    }
    @GetMapping("/addmaintain")
    public R<String> AddMaintain(HttpServletRequest request, String phone ,int userId,String description ,String date){
        LambdaQueryWrapper<BroadbandCustomer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BroadbandCustomer::getPhone,phone);

        BroadbandCustomer b = broadbandCustomerService.getOne(queryWrapper);
        if (b==null) return R.error("未找到该号码的客户");
        Maintain maintain = new Maintain();
        maintain.setUserId(userId);
        maintain.setDescription(description);
        maintain.setCustomerId(b.getId());
        if(date!=null&&date.length()>=1)
            maintain.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        maintainService.save(maintain);
        return R.success("添加成功");
    }

    @GetMapping("/getbyid")
    public R<Maintain> getMaintainById(int id){
        Maintain maintain = maintainService.getById(id);
        return R.success(maintain);
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
                maintainService.removeById(id);
            }
            return R.success("删除成功");
        }
        return R.error("权限不足");
    }
}
