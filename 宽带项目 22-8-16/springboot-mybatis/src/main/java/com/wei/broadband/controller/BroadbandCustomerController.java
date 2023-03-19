package com.wei.broadband.controller;

import com.wei.broadband.common.R;
import com.wei.broadband.dto.BroadbandDTO;

import com.wei.broadband.dto.DTO;
import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.service.BroadbandCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/broadband")
@Slf4j
public class BroadbandCustomerController {
    @Autowired
    private BroadbandCustomerService broadbandCustomerService;

    @Autowired
    private DTO dto;


    @GetMapping("getMyCusList")
    public R<List<BroadbandDTO>> getMyCusList(HttpServletRequest request){
        int id = (int)request.getSession().getAttribute("user");
        List<BroadbandDTO> dtoList = broadbandCustomerService.getMyCusList(id);
        return R.success(dtoList);
    }

    @GetMapping("list")
    public R<List<BroadbandDTO>> list(HttpServletRequest request, String content, Integer sort, Integer select,Integer current,Integer size){

        List<BroadbandCustomer> broadbandCustomers = broadbandCustomerService.cusList(request, content, sort, select,current,size);
        return R.success(dto.broadbandDTO(broadbandCustomers));
    }
    @GetMapping("count")
    public R<Integer> count(String content, int sort, int select){

        int count = broadbandCustomerService.getCusCount(content,sort,select);

        return R.success(count);
    }





    @PostMapping("add")
    public R<String> save(HttpServletRequest request,@RequestBody BroadbandCustomer broadbandCustomer){
        int id = (int)request.getSession().getAttribute("user");

        return broadbandCustomerService.addCus(id,broadbandCustomer);

    }

    @GetMapping("getByPhone")
    public R<BroadbandCustomer> getByPhone(String phone){

        BroadbandCustomer customer = broadbandCustomerService.getByPhone(phone);


        if (customer!=null)
            return R.success(customer);
        return R.error("未找到该用户");
    }


    @GetMapping("getbyid")
    public R<BroadbandCustomer> getById(int id){
        BroadbandCustomer broadbandCustomer = broadbandCustomerService.getById(id);
        return R.success(broadbandCustomer);
    }

    @GetMapping("addbroadband")
    public R<String> addBroadband(int id,String number){

        broadbandCustomerService.addBroadband(id,number);
        return R.success("添加成功");
    }

    @GetMapping("broadbandlist")
    public R<List<BroadbandCustomer>> BroadbandList(){

        List<BroadbandCustomer> list =broadbandCustomerService.broadbandList();



        return R.success(list);
    }

    @PostMapping("updata")
    public R<String> updata(HttpServletRequest request,@RequestBody BroadbandCustomer broadbandCustomer){


        int id = (int)request.getSession().getAttribute("user");
        return broadbandCustomerService.updateCus(id,broadbandCustomer);


    }
    @DeleteMapping
    public R<String> delete(HttpServletRequest request,int id){
        int userid = (int)request.getSession().getAttribute("user");
        return broadbandCustomerService.deleteCus(userid,id);





    }






}
