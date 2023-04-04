package com.wei.broadband.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.broadband.common.R;
import com.wei.broadband.po.PaymentsEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 
 *
 * @author wei
 * @email 
 * @date 2023-02-19 14:07:31
 */
public interface PaymentsService extends IService<PaymentsEntity> {

    R<List<PaymentsEntity>> getMyPaymentsList(HttpServletRequest request, String content, String type, String startTime, String endTime, Integer status);

    R<List<PaymentsEntity>> getPaymentsList(String content, String type, String startTime, String endTime, Integer status, Integer userId);

    R<String> enablePayment(int id);

    R<String> checkBalance();
}

