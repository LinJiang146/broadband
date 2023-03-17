package com.wei.broadband.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.DataMap;
import com.wei.broadband.common.ExcelHandle;
import com.wei.broadband.common.R;
import com.wei.broadband.po.Package;
import com.example.springbootmybatis.po.*;
import com.wei.broadband.po.excelPo.ExcelCustomerData;
import com.example.springbootmybatis.service.*;
import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.Commission;
import com.wei.broadband.po.User;
import com.wei.broadband.service.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.ss.usermodel.Workbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
@RequestMapping("/common")
public class CommonController {

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

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + suffix;

        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (suffix.equals(".xlsx") || suffix.equals(".xls")) {
            file.transferTo(new File(basePath + originalFilename));
            return R.success("上传成功");
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

        return R.success(filename);
    }

    @PostMapping("/uploadImg")
    public R<String> uploadImg(MultipartFile file) throws IOException {
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

        return R.success(filename);
    }
//    @GetMapping("finishmaintain")
//    public R<String> FinishMaintain(int id,MultipartFile file) throws IOException {
//
//        String originalFilename = file.getOriginalFilename();
//        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String filename= UUID.randomUUID().toString()+suffix;
//
//        File dir = new File(basePath);
//        if(!dir.exists()){
//            dir.mkdirs();
//        }
//
//
//        try {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            Thumbnails.of(file.getInputStream())
//                    .scale(1.0f).outputQuality(0.3f)
//                    .toOutputStream(outputStream);
//            byte[] bytes = outputStream.toByteArray();
//            InputStream inputStream = new ByteArrayInputStream(bytes);
//            MultipartFile files = new MockMultipartFile(file.getOriginalFilename(), file.getOriginalFilename(), file.getContentType(), inputStream);
//            files.transferTo(new File(basePath+filename));
//
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//
//        return R.success(filename);
//
//        Maintain maintain = maintainService.getById(id);
//        maintain.set
//
//
//        int p = userService.getById(id).getPermission();
//        LambdaQueryWrapper<Maintain> queryWrapper = new LambdaQueryWrapper<>();
//        if(p>1){
//            queryWrapper.isNull(Maintain::getImage).le(Maintain::getDate, LocalDate.now()).eq(Maintain::getUserId,id);
//        }
//        else {
//            queryWrapper.isNull(Maintain::getImage).le(Maintain::getDate,LocalDate.now());
//        }
//        List<Maintain> list = maintainService.list(queryWrapper);
//        return R.success(list);
//    }

    @GetMapping("/download")
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

//    @PostMapping("/excel")
//    public R<String> getExcel(MultipartFile file) throws IOException {
//
//        List<BroadbandCustomer> cusList = broadbandCustomerService.list();
//        List<User> userList = userService.list();
//        List<Package> packageList = packageService.list();
//
//        ImportParams params = new ImportParams();
//        params.setTitleRows(0);
//        params.setHeadRows(1);
//        String originalFilename = file.getOriginalFilename();
//        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String filename = UUID.randomUUID().toString() + suffix;
//        file.transferTo(new File(basePath + filename));
//        List<InExcel> list = ExcelImportUtil.importExcel(new File(basePath + filename),
//                InExcel.class, params);
//
//        List<BroadbandCustomer> broadbandCustomerList = new ArrayList<>();
//        for (BroadbandCustomer broadbandCustomer :
//                cusList) {
//            if (broadbandCustomerList.size() == 0) broadbandCustomerList.add(broadbandCustomer);
//            else {
//                for (int i = 0; i < broadbandCustomerList.size(); i++) {
//                    if (broadbandCustomer.getPhone().equals(broadbandCustomerList.get(i).getPhone())) break;
//                    if (i == broadbandCustomerList.size() - 1) {
//                        broadbandCustomerList.add(broadbandCustomer);
//                    }
//                }
//            }
//
//
//        }
//
//
//        for (BroadbandCustomer b :
//                broadbandCustomerList) {
//
//            FcDetails fcDetails;
//            for (InExcel in :
//                    list) {
//                if (!"2021贵港合伙人第三档10万元任务60%分成政策".equals(in.getPolicyName())) continue;
//                if (b.getPhone().equals(in.getFazhanNumber())) {
//                    fcDetails = new FcDetails();
//                    fcDetails.setCreatDate(b.getCreateTime().toLocalDate().toString());
//                    fcDetails.setPhone(b.getPhone());
//                    fcDetails.setName(b.getName());
//                    fcDetails.setAmount1(in.getAmount());
//                    fcDetails.setDate(in.getDate());
//                    fcDetails.setAddress(b.getAddress());
//                    for (Package p :
//                            packageList) {
//                        if (p.getId() == b.getPackageId()) {
//                            fcDetails.setPackageName(p.getName());
//                            break;
//                        }
//                    }
//                    for (User user :
//                            userList) {
//                        if (user.getId() == b.getCreateUser()) {
//                            fcDetails.setDevelop(user.getName());
//                            fcDetails.setCreatUserId(user.getId());
//                            break;
//                        }
//                    }
//
//                    for (InExcel i :
//                            list) {
//                        if (!"2021贵港合伙人第三档10万元任务60%分成政策".equals(in.getPolicyName())) continue;
//                        if (i.getFazhanNumber() != null && i.getFazhanNumber().equals(b.getBroadbandNumber()) && i.getDate().equals(in.getDate())) {
//                            fcDetails.setAmount2(i.getAmount());
//                            fcDetails.setBroadbandNumber(b.getBroadbandNumber());
//                            break;
//                        }
//                    }
//                    fcDetails.setAmount(fcDetails.getAmount1() + fcDetails.getAmount2());
//                    int id1 = fcDetails.getCreatUserId();
//                    int id2 = userService.getById(id1).getCreateUser();
//                    int id3 = userService.getById(id2).getCreateUser();
//                    float amount1 = fcDetails.getAmount1() * 100 / 60;
//                    float amount2 = fcDetails.getAmount2() * 100 / 60;
//                    float amount = fcDetails.getAmount() * 100 / 60;
//                    if (id1 == 1) {
//                        fcDetailsService.save(fcDetails);
//                    } else if (id2 == 1) {
//                        fcDetails.setAmount1(amount1 * 48 / 100);
//                        fcDetails.setAmount2(amount2 * 48 / 100);
//                        fcDetails.setAmount(amount * 48 / 100);
//                        fcDetailsService.save(fcDetails);
//
//                        fcDetails.setType(1);
//
//                        fcDetails.setAmount1(amount1 * 12 / 100);
//                        fcDetails.setAmount2(amount2 * 12 / 100);
//                        fcDetails.setAmount(amount * 12 / 100);
//                        fcDetails.setCreatUserId(id2);
//                        fcDetailsService.save(fcDetails);
//                    } else if (id3 == 1) {
//                        fcDetails.setAmount1(amount1 * 42 / 100);
//                        fcDetails.setAmount2(amount2 * 42 / 100);
//                        fcDetails.setAmount(amount * 42 / 100);
//                        fcDetailsService.save(fcDetails);
//
//                        fcDetails.setType(1);
//
//                        fcDetails.setAmount1(amount1 * 6 / 100);
//                        fcDetails.setAmount2(amount2 * 6 / 100);
//                        fcDetails.setAmount(amount * 6 / 100);
//                        fcDetails.setCreatUserId(id2);
//                        fcDetailsService.save(fcDetails);
//
//                        fcDetails.setAmount1(amount1 * 12 / 100);
//                        fcDetails.setAmount2(amount2 * 12 / 100);
//                        fcDetails.setAmount(amount * 12 / 100);
//                        fcDetails.setCreatUserId(id3);
//                        fcDetailsService.save(fcDetails);
//                    } else {
//                        fcDetails.setAmount1(amount1 * 36 / 100);
//                        fcDetails.setAmount2(amount2 * 36 / 100);
//                        fcDetails.setAmount(amount * 36 / 100);
//                        fcDetailsService.save(fcDetails);
//
//                        fcDetails.setType(1);
//
//                        fcDetails.setAmount1(amount1 * 6 / 100);
//                        fcDetails.setAmount2(amount2 * 6 / 100);
//                        fcDetails.setAmount(amount * 6 / 100);
//                        fcDetails.setCreatUserId(id2);
//                        fcDetailsService.save(fcDetails);
//
//                        fcDetails.setAmount1(amount1 * 6 / 100);
//                        fcDetails.setAmount2(amount2 * 6 / 100);
//                        fcDetails.setAmount(amount * 6 / 100);
//                        fcDetails.setCreatUserId(id3);
//                        fcDetailsService.save(fcDetails);
//
//                        fcDetails.setAmount1(amount1 * 12 / 100);
//                        fcDetails.setAmount2(amount2 * 12 / 100);
//                        fcDetails.setAmount(amount * 12 / 100);
//                        fcDetails.setCreatUserId(1);
//                        fcDetailsService.save(fcDetails);
//                    }
//                }
//            }
//        }
//
//        List<FcDetails> fcDetails = fcDetailsService.list();
//
//        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),
//                FcDetails.class, fcDetails);
////        ClassPathResource classPathResource = new ClassPathResource("C:/excel/发展清单.xls");
////        File inputStream = classPathResource.getFile();
////        String excelPath = inputStream.getPath();
////        System.out.println(excelPath);
//        FileOutputStream fos = new FileOutputStream("C:/excel/发展清单.xls");
//        workbook.write(fos);
//        fos.close();
//        return R.success(originalFilename);
//    }

    @PostMapping("/getCustomerFromExcel")
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




    @GetMapping("/remove")
    public R<String> removeFcDetails() throws IOException {
        LambdaQueryWrapper<Commission> queryWrapper = new LambdaQueryWrapper<>();
        commissionService.remove(queryWrapper);

        List<Commission> fcDetails = commissionService.list();

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),
                Commission.class, fcDetails);
//        ClassPathResource classPathResource = new ClassPathResource("C:/excel/发展清单.xls");
//        File inputStream = classPathResource.getFile();
//        String excelPath = inputStream.getPath();
//        System.out.println(excelPath);
        FileOutputStream fos = new FileOutputStream("C:/excel/发展清单.xls");
        workbook.write(fos);
        fos.close();

        return R.success("清空成功");

    }

    @PostMapping("/downloadexcel")
    public void download(HttpServletResponse res) throws Exception {
        File excelFile = new File("C:/excel/发展清单.xls");
        res.setCharacterEncoding("UTF-8");
        String realFileName = excelFile.getName();
        res.setHeader("content-type", "application/octet-stream;charset=UTF-8");
        res.setContentType("application/octet-stream;charset=UTF-8");
        //加上设置大小下载下来的.xlsx文件打开时才不会报“Excel 已完成文件级验证和修复。此工作簿的某些部分可能已被修复或丢弃”
        res.addHeader("Content-Length", String.valueOf(excelFile.length()));
        try {
            res.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(realFileName.trim(), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(excelFile));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
