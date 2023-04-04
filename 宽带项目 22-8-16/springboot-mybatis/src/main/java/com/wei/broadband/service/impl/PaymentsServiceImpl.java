package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.UserBalanceDTO;
import com.wei.broadband.mapper.PaymentsDao;
import com.wei.broadband.po.PaymentsEntity;
import com.wei.broadband.po.User;
import com.wei.broadband.po.WithdrawalsEntity;
import com.wei.broadband.service.PaymentsService;
import com.wei.broadband.service.UserService;
import com.wei.broadband.service.WithdrawalsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("paymentsService")
public class PaymentsServiceImpl extends ServiceImpl<PaymentsDao, PaymentsEntity> implements PaymentsService {
    @Autowired
    private WithdrawalsService withdrawalsService;

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private UserService userService;

    @Override
    public R<List<PaymentsEntity>> getMyPaymentsList(HttpServletRequest request, String content, String type, String startTime, String endTime, Integer status) {
        LambdaQueryWrapper<PaymentsEntity> queryWrapper = new LambdaQueryWrapper<>();
        int id = (int)request.getSession().getAttribute("user");
        queryWrapper.eq(PaymentsEntity::getUserId,id);
        queryWrapper.orderByDesc(PaymentsEntity::getDateTime);
        return R.success(paymentsService.list(queryWrapper));
    }

    @Override
    public R<List<PaymentsEntity>> getPaymentsList(String content, String type, String startTime, String endTime, Integer status, Integer userId) {
        LambdaQueryWrapper<PaymentsEntity> queryWrapper = new LambdaQueryWrapper<>();



        queryWrapper.eq(StringUtils.isNotEmpty(type),PaymentsEntity::getType,type)
                .eq(status!=null,PaymentsEntity::getStatus,status)
                .eq(userId!=null,PaymentsEntity::getUserId,userId);

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotEmpty(startTime)){
            queryWrapper.ge(PaymentsEntity::getDateTime, LocalDateTime.parse(startTime,pattern));
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

    @Override
    public R<String> enablePayment(int id) {
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

    @Override
    public R<String> checkBalance() {
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
        Map<Integer, UserBalanceDTO> balanceDTOMap = new HashMap<>();
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