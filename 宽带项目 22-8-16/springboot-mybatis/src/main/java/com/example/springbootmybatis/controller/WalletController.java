package com.example.springbootmybatis.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springbootmybatis.common.R;
import com.example.springbootmybatis.dto.UserBalanceDTO;
import com.example.springbootmybatis.po.MissionEntity;
import com.example.springbootmybatis.po.PaymentsEntity;
import com.example.springbootmybatis.po.User;
import com.example.springbootmybatis.po.WithdrawalsEntity;
import com.example.springbootmybatis.service.PaymentsService;
import com.example.springbootmybatis.service.UserService;
import com.example.springbootmybatis.service.WalletItemService;

import com.example.springbootmybatis.service.WithdrawalsService;
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
        LambdaQueryWrapper<PaymentsEntity> queryWrapper = new LambdaQueryWrapper<>();

        int id = (int)request.getSession().getAttribute("user");
        queryWrapper.eq(PaymentsEntity::getUserId,id);



//        queryWrapper.eq(StringUtils.isNotEmpty(type),PaymentsEntity::getType,type)
//                .eq(status!=null,PaymentsEntity::getStatus,status);


//        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        if (StringUtils.isNotEmpty(startTime)){
//            queryWrapper.ge(PaymentsEntity::getDateTime,LocalDateTime.parse(startTime,pattern));
//        }
//        if (StringUtils.isNotEmpty(endTime)){
//            queryWrapper.le(PaymentsEntity::getDateTime,LocalDateTime.parse(endTime,pattern));
//        }
//        if (StringUtils.isNotEmpty(content)){
//            queryWrapper.and((i)->{ i.like(PaymentsEntity::getDescription,content)
//                            .or().like(PaymentsEntity::getUserName,content);
//                    }
//            );
//        }
        queryWrapper.orderByDesc(PaymentsEntity::getDateTime);

        return R.success(paymentsService.list(queryWrapper));
    }


    @GetMapping("getPaymentsList")
    public R<List<PaymentsEntity>> getPaymentsList(String content,String type,String startTime,String endTime,Integer status,Integer userId){
        LambdaQueryWrapper<PaymentsEntity> queryWrapper = new LambdaQueryWrapper<>();



        queryWrapper.eq(StringUtils.isNotEmpty(type),PaymentsEntity::getType,type)
                .eq(status!=null,PaymentsEntity::getStatus,status)
                .eq(userId!=null,PaymentsEntity::getUserId,userId);

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotEmpty(startTime)){
            queryWrapper.ge(PaymentsEntity::getDateTime,LocalDateTime.parse(startTime,pattern));
        }
        if (StringUtils.isNotEmpty(endTime)){
            queryWrapper.le(PaymentsEntity::getDateTime,LocalDateTime.parse(endTime,pattern));
        }
        if (StringUtils.isNotEmpty(content)){
            queryWrapper.and((i)->{ i.like(PaymentsEntity::getDescription,content)
                            .or().like(PaymentsEntity::getUserName,content);
                    }
            );
        }

        queryWrapper.orderByDesc(PaymentsEntity::getDateTime);

        return R.success(paymentsService.list(queryWrapper));
    }



    @GetMapping("getWithdrawals")
    public R<List<WithdrawalsEntity>> getWithdrawals(){
        return R.success(withdrawalsService.list(
                new LambdaQueryWrapper<WithdrawalsEntity>()
                        .orderByDesc(WithdrawalsEntity::getDateTime)));
    }

    @GetMapping("getMyWithdrawals")
    public R<List<WithdrawalsEntity>> getMyWithdrawals(HttpServletRequest request){
        int id = (int)request.getSession().getAttribute("user");
        LambdaQueryWrapper<WithdrawalsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WithdrawalsEntity::getUserId,id);
        queryWrapper.orderByDesc(WithdrawalsEntity::getDateTime);
        return R.success(withdrawalsService.list(queryWrapper));
    }

    @GetMapping("withdrawals")
    public R<String> withdrawals(HttpServletRequest request,Double amount){

        int id = (int)request.getSession().getAttribute("user");


        //检测余额是否足够
        User user = userService.getById(id);
        if (user.getBalance()<amount){
            return R.error("余额不足");
        }
        //判断是否有以存在的待审核提现
        WithdrawalsEntity one = withdrawalsService.getOne(new LambdaQueryWrapper<WithdrawalsEntity>()
                .eq(WithdrawalsEntity::getStatus, 0)
                .eq(WithdrawalsEntity::getUserId, id));
        if (one!=null){
            return R.error("已有待处理的提现");
        }



        //判断余额是否足够
        LambdaQueryWrapper<PaymentsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentsEntity::getUserId, id)
                .eq(PaymentsEntity::getStatus,1);
        List<PaymentsEntity> paymentsList = paymentsService.list(queryWrapper);

        LambdaQueryWrapper<WithdrawalsEntity> withdrawalsQueryWrapper = new LambdaQueryWrapper<>();
        withdrawalsQueryWrapper.eq(WithdrawalsEntity::getUserId,id)
                .eq(WithdrawalsEntity::getStatus,1);
        List<WithdrawalsEntity> withdrawalsList = withdrawalsService.list(withdrawalsQueryWrapper);


        Double paymentCount = 0d;
        Double withdrawalsCount = 0d;
        for (PaymentsEntity paymentsEntity : paymentsList) {
            paymentCount+=paymentsEntity.getAmount();
        }
        for (WithdrawalsEntity withdrawalsEntity : withdrawalsList) {
            withdrawalsCount+=withdrawalsEntity.getAmount();
        }

        //判断并提现
        if (paymentCount-withdrawalsCount>amount){
            WithdrawalsEntity withdrawalsEntity = new WithdrawalsEntity();
            withdrawalsEntity.setAmount(amount);
            withdrawalsEntity.setUserId(id);
            withdrawalsEntity.setStatus(0);
            withdrawalsEntity.setDateTime(LocalDateTime.now());
            withdrawalsService.save(withdrawalsEntity);
            return R.success("已发送申请，待审核");
        }
        return R.error("余额不足");
    }

    @GetMapping("payWithdrawals")
    public R<String> payWithdrawals(int WithdrawalsId,String billingImg,String remark){
        WithdrawalsEntity withdrawals = withdrawalsService.getById(WithdrawalsId);
        withdrawals.setStatus(1);
        withdrawals.setBillingImg(billingImg);
        withdrawals.setRemark(remark);
        withdrawals.setProcessDateTime(LocalDateTime.now());
        withdrawalsService.updateById(withdrawals);

        User user = userService.getById(withdrawals.getUserId());
        user.setBalance(user.getBalance()-withdrawals.getAmount());
        userService.updateById(user);
        return R.success("打款记录保存成功");
    }
    @DeleteMapping("deleteWithdrawals")
    public R<String> deleteWithdrawals(int WithdrawalsId,String remark){

        WithdrawalsEntity withdrawals = withdrawalsService.getById(WithdrawalsId);
        withdrawals.setStatus(2);
        withdrawals.setRemark(remark);
        withdrawals.setProcessDateTime(LocalDateTime.now());
        withdrawalsService.updateById(withdrawals);

        return R.success("驳回提现成功");
    }

    @DeleteMapping("enablePayment")
    public R<String> enablePayment(int id){
        PaymentsEntity payment = paymentsService.getById(id);
        payment.setStatus(1- payment.getStatus());
        paymentsService.updateById(payment);
        User user = userService.getById(payment.getUserId());
        if (payment.getStatus()==1){
            user.setBalance(user.getBalance()+ payment.getAmount());
        }else {
            user.setBalance(user.getBalance()- payment.getAmount());
        }
        userService.updateById(user);

        return R.success("修改成功");
    }



    //余额矫正
    @GetMapping("checkBalance")
    public R<String> checkBalance(){
        //数据准备
        List<PaymentsEntity> paymentsEntityList = paymentsService.list(
                new LambdaQueryWrapper<PaymentsEntity>()
                        .eq(PaymentsEntity::getStatus,1)
        );
        List<WithdrawalsEntity> withdrawalsEntityList = withdrawalsService.list(
                new LambdaQueryWrapper<WithdrawalsEntity>()
                        .eq(WithdrawalsEntity::getStatus,1)
        );
        List<User> userList = userService.list();


        //计算余额
        Map<Integer,UserBalanceDTO> balanceDTOMap = new HashMap<>();
        for (User user : userList) {
            UserBalanceDTO balanceDTO = new UserBalanceDTO();
            balanceDTO.setUserId(user.getId());
            balanceDTO.setBalance(0d);
            balanceDTOMap.put(user.getId(), balanceDTO);
        }
        for (PaymentsEntity paymentsEntity : paymentsEntityList) {
            UserBalanceDTO balanceDTO = balanceDTOMap.get(paymentsEntity.getUserId());
            balanceDTO.setBalance(balanceDTO.getBalance()+paymentsEntity.getAmount());
        }
        for (WithdrawalsEntity withdrawalsEntity : withdrawalsEntityList) {
            UserBalanceDTO balanceDTO = balanceDTOMap.get(withdrawalsEntity.getUserId());
            balanceDTO.setBalance(balanceDTO.getBalance()- withdrawalsEntity.getAmount());
        }
        //检查并矫正余额
        int n = 0;
        for (User user : userList) {
            UserBalanceDTO balanceDTO = balanceDTOMap.get(user.getId());
            if (!balanceDTO.getBalance().equals(user.getBalance())){
                user.setBalance(balanceDTO.getBalance());
                userService.updateById(user);
                n++;
            }
        }
        if(n==0){
            return R.success("检测未发现异常，请人工确认，若出现异常请联系管理员");
        }
        else {
            return R.error("发现"+n+"个异常余额并进行矫正，请联系管理员");
        }
    }

}
