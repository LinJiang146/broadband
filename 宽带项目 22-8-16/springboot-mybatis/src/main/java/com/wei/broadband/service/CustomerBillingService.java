package com.wei.broadband.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.CusBillDTO;
import com.wei.broadband.po.CustomerBilling;

import java.util.List;

/**
 * 
 *
 * @author wei
 * @email 
 * @date 2023-02-05 12:45:32
 */
public interface CustomerBillingService extends IService<CustomerBilling> {

    R<String> addBillList(List<CustomerBilling> billingList);

    List<String> getBillDate();

    List<CusBillDTO> getCusBillData(String date);
}

