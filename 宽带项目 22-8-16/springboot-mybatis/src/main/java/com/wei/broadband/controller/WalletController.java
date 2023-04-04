package com.wei.broadband.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.UserBalanceDTO;
import com.wei.broadband.po.PaymentsEntity;
import com.wei.broadband.po.User;
import com.wei.broadband.po.WithdrawalsEntity;
import com.wei.broadband.service.PaymentsService;
import com.wei.broadband.service.UserService;

import com.wei.broadband.service.WithdrawalsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 *
 * @author wei
 * @email 
 * @date 2023-02-19 14:07:31
 */
@RestController
@RequestMapping("wallet")
public class WalletController {


    @Autowired
    private WithdrawalsService withdrawalsService;

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private UserService userService;


    @GetMapping("getMyPaymentsList")
    public R<List<PaymentsEntity>> getMyPaymentsList(HttpServletRequest request, String content, String type, String startTime, String endTime, Integer status){

        return paymentsService.getMyPaymentsList(request,content,type,startTime,endTime,status);



    }


    @GetMapping("getPaymentsList")
    public R<List<PaymentsEntity>> getPaymentsList(String content,String type,String startTime,String endTime,Integer status,Integer userId){

        return paymentsService.getPaymentsList(content,type,startTime,endTime,status,userId);




    }



    @GetMapping("getWithdrawals")
    public R<List<WithdrawalsEntity>> getWithdrawals(){
        return withdrawalsService.getWithdrawals();


    }

    @GetMapping("getMyWithdrawals")
    public R<List<WithdrawalsEntity>> getMyWithdrawals(HttpServletRequest request){
        return withdrawalsService.getMyWithdrawals(request);



    }

    @GetMapping("withdrawals")
    public R<String> withdrawals(HttpServletRequest request,Double amount){

        return withdrawalsService.withdrawals(request,amount);



    }

    @GetMapping("payWithdrawals")
    public R<String> payWithdrawals(int WithdrawalsId,String billingImg,String remark){
        return withdrawalsService.payWithdrawals(WithdrawalsId,billingImg,remark);




    }
    @DeleteMapping("deleteWithdrawals")
    public R<String> deleteWithdrawals(int WithdrawalsId,String remark){

        return withdrawalsService.deleteWithdrawals(WithdrawalsId,remark);



    }

    @DeleteMapping("enablePayment")
    public R<String> enablePayment(int id){

        return paymentsService.enablePayment(id);



    }



    //余额矫正
    @GetMapping("checkBalance")
    public R<String> checkBalance(){

        return paymentsService.checkBalance();



    }

}
