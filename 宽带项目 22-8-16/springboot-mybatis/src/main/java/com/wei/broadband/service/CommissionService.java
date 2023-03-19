package com.wei.broadband.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.broadband.common.R;
import com.wei.broadband.po.Commission;
import com.wei.broadband.po.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CommissionService extends IService<Commission> {
    List<User> getFcUser(int id);

    List<Commission> getList(int id, String date1, String date2, int type);

    R<String> getPortFeesExcel(MultipartFile file) throws IOException;

    R<String> getCommissionExcel(MultipartFile file) throws IOException;

    List<String> getFcDate();
}
