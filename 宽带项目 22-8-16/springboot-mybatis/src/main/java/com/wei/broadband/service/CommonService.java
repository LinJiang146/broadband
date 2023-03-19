package com.wei.broadband.service;

import com.wei.broadband.common.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CommonService{
    String upload(MultipartFile file) throws IOException;

    String uploadImg(MultipartFile file);

    void download(String name, HttpServletResponse response);

    R<String> getCustomerFromExcel(MultipartFile file) throws IOException;
}
