package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.R;
import com.wei.broadband.mapper.WithdrawalsDao;
import com.wei.broadband.po.PaymentsEntity;
import com.wei.broadband.po.User;
import com.wei.broadband.po.WithdrawalsEntity;
import com.wei.broadband.service.PaymentsService;
import com.wei.broadband.service.UserService;
import com.wei.broadband.service.WithdrawalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;


@Service("withdrawalsService")
public class WithdrawalsServiceImpl extends ServiceImpl<WithdrawalsDao, WithdrawalsEntity> implements WithdrawalsService {
    @Autowired
    private WithdrawalsService withdrawalsService;

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private UserService userService;

    @Override
    public R<List<WithdrawalsEntity>> getWithdrawals() {
        return R.success(withdrawalsService.list(
                new LambdaQueryWrapper<WithdrawalsEntity>()
                        .orderByDesc(WithdrawalsEntity::getDateTime)));
    }

    @Override
    public R<List<WithdrawalsEntity>> getMyWithdrawals(HttpServletRequest request) {
        int id = (int)request.getSession().getAttribute("user");
        LambdaQueryWrapper<WithdrawalsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WithdrawalsEntity::getUserId,id);
        queryWrapper.orderByDesc(WithdrawalsEntity::getDateTime);
        return R.success(withdrawalsService.list(queryWrapper));
    }

    @Override
    public R<String> withdrawals(HttpServletRequest request, Double amount) {
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

    @Override
    public R<String> payWithdrawals(int withdrawalsId, String billingImg, String remark) {
        WithdrawalsEntity withdrawals = withdrawalsService.getById(withdrawalsId);
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

    @Override
    public R<String> deleteWithdrawals(int withdrawalsId, String remark) {
        WithdrawalsEntity withdrawals = withdrawalsService.getById(withdrawalsId);
        withdrawals.setStatus(2);
        withdrawals.setRemark(remark);
        withdrawals.setProcessDateTime(LocalDateTime.now());
        withdrawalsService.updateById(withdrawals);

        return R.success("驳回提现成功");
    }
}