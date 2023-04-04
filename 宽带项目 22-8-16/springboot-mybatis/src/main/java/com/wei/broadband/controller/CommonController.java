package com.wei.broadband.controller;


import com.wei.broadband.common.R;
import com.wei.broadband.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@CrossOrigin
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {

        String filename = commonService.upload(file);

        return R.success(filename);
    }

    @PostMapping("/uploadImg")
    public R<String> uploadImg(MultipartFile file) {
        String filename = commonService.uploadImg(file);
        return R.success(filename);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        commonService.download(name, response);
    }



    @PostMapping("/getCustomerFromExcel")
    public R<String> getCustomerFromExcel(MultipartFile file) throws IOException {
        return commonService.getCustomerFromExcel(file);
    }



}
