package com.wei.broadband.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.BroadbandDTO;
import com.wei.broadband.po.BroadbandCustomer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BroadbandCustomerService extends IService<BroadbandCustomer> {

    List<BroadbandDTO> getMyCusList(int id);

    List<BroadbandCustomer> cusList(HttpServletRequest request, String content, Integer sort, Integer select, Integer current, Integer size);

    int getCusCount(String content, int sort, int select);

    R<String> addCus(int id, BroadbandCustomer broadbandCustomer);

    BroadbandCustomer getByPhone(String phone);

    R<String> deleteCus(int userid, int id);

    R<String> updateCus(int id, BroadbandCustomer broadbandCustomer);

    List<BroadbandCustomer> broadbandList();

    void addBroadband(int id, String number);
}
