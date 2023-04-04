package com.wei.broadband.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.broadband.common.R;
import com.wei.broadband.po.WithdrawalsEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 
 *
 * @author wei
 * @email 
 * @date 2023-02-19 14:07:31
 */
public interface WithdrawalsService extends IService<WithdrawalsEntity> {


    R<List<WithdrawalsEntity>> getWithdrawals();

    R<List<WithdrawalsEntity>> getMyWithdrawals(HttpServletRequest request);

    R<String> withdrawals(HttpServletRequest request, Double amount);

    R<String> payWithdrawals(int withdrawalsId, String billingImg, String remark);

    R<String> deleteWithdrawals(int withdrawalsId, String remark);
}

