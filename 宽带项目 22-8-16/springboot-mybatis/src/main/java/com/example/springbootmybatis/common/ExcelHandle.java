package com.example.springbootmybatis.common;



import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ExcelHandle {

    private static String basePath = "C://hello1/";
    private static String textPath = "C://text/";

    public static <T extends Object> List<T> getListFormExcel(MultipartFile file,Class<?> outputClass) throws IOException {
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + suffix;
        file.transferTo(new File(basePath + filename));
        List<T> list = ExcelImportUtil.importExcel(new File(basePath + filename),
                outputClass, params);
        return list;
    }

}
