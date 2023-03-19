package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.DataMap;
import com.wei.broadband.common.ExcelHandle;
import com.wei.broadband.common.R;
import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.Package;
import com.wei.broadband.po.User;
import com.wei.broadband.po.excelPo.ExcelCustomerData;
import com.wei.broadband.service.*;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private BroadbandCustomerService broadbandCustomerService;

    @Autowired
    private UserService userService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private MaintainService maintainService;

    @Resource
    private ModelMapper modelMapper;

    private String basePath = "C://hello1/";
    private String textPath = "C://text/";

    @Resource
    private DataMap dataMap;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + suffix;

        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (suffix.equals(".xlsx") || suffix.equals(".xls")) {
            file.transferTo(new File(basePath + originalFilename));
            return filename;
        }


        return "类型错误";
    }

    @Override
    public String uploadImg(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + suffix;

        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .scale(1.0f).outputQuality(0.3f)
                    .toOutputStream(outputStream);
            byte[] bytes = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            MultipartFile files = new MockMultipartFile(file.getOriginalFilename(), file.getOriginalFilename(), file.getContentType(), inputStream);
            files.transferTo(new File(basePath + filename));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    @Override
    public void download(String name, HttpServletResponse response) {
        String path = basePath + name;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    @Override
    public R<String> getCustomerFromExcel(MultipartFile file) throws IOException {
        //查找已有数据为导入数据做准备
        Map<String, BroadbandCustomer> customerMap = dataMap.getPhone2CusMap();

        Map<String, User> name2UserMap = dataMap.getName2UserMap();

        Map<String, Package> name2PackageMap = dataMap.getName2PackageMap();

        //转化Excel数据为列表对象数据
        List<ExcelCustomerData> list = ExcelHandle.getListFormExcel(file,ExcelCustomerData.class);

        int num = 0;
        int pnum = 0;
        int broadnum = 0;

        for (ExcelCustomerData data : list) {
            BroadbandCustomer cus = customerMap.get(data.getPhone());

            //填充空白的宽带号
            if (cus != null && data.getBroadbandNumber() != null && !"".equals(data.getBroadbandNumber())) {
                if (cus.getBroadbandNumber() == null || "".equals(cus.getBroadbandNumber())) {
                    cus.setBroadbandNumber(data.getBroadbandNumber());
                    broadbandCustomerService.saveOrUpdate(cus);
                    broadnum++;
                }
            }

            if (cus == null && data.getCreatUserName() != null && data.getPackageName() != null) {

                //设置数据对应
                data.setTypeName("异网".equals(data.getTypeName()) ? "0" : "1");
//                data.setStatusName("1");
                cus = modelMapper.map(data, BroadbandCustomer.class);

                Package aPackage = name2PackageMap.get(data.getPackageName());
                Integer packageId = null;
                if (aPackage!=null){
                    packageId = aPackage.getId();
                }

                User user = name2UserMap.get(data.getCreatUserName().replaceAll("[^\u4E00-\u9FA5]", ""));
                Integer userId = null ;
                if (user!=null) {
                    userId = user.getId();
                }


                //未找到套餐着添加套餐，名字未含价格的套餐略过
                if (packageId == null) {
                    String pattern = "[0-9][0-9]*(?=元)";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(data.getPackageName());
                    if (m.find()) {
                        Integer price = Integer.parseInt(m.group());
                        Package p = new Package();
                        p.setName(data.getPackageName());
                        p.setPrice(price);
                        p.setType(1);
                        p.setCommission(0);
                        packageService.save(p);
                        p = packageService.getOne(new LambdaQueryWrapper<Package>().eq(Package::getName, p.getName()));
                        name2PackageMap.put(p.getName(), p);
                        packageId = p.getId();
                        pnum++;
                    }
                }


                //增加客户
                if (packageId != null && userId != null) {
                    cus.setCreateUser(userId);
                    cus.setPackageId(packageId);
                    broadbandCustomerService.save(cus);
                    cus = broadbandCustomerService.getOne(new LambdaQueryWrapper<BroadbandCustomer>().eq(BroadbandCustomer::getPhone,cus.getPhone()));
                    customerMap.put(cus.getPhone(), cus);
                    num++;
                }
            }
        }
        return R.success("新增" + num + "条客户数据\n" + "新增" + pnum + "个套餐数据"+"为已有客户补充宽带号"+broadnum+"条");
    }
}
