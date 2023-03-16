package com.example.springbootmybatis.po.excelPo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class ExcelCustomerData {

    @Excel(name = "用户姓名")
    private String name;

    @Excel(name = "用户号码")
    private String phone;

    @Excel(name = "办理产品")
    private String packageName;

    @Excel(name = "固网")
    private String broadbandNumber;

    @Excel(name = "受理人")
    private String creatUserName;

    @Excel(name = "用户类型")
    private String typeName;
}
