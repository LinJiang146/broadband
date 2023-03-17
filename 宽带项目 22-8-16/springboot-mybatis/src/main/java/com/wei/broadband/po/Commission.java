package com.wei.broadband.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class Commission {

    private int id;

    @Excel(name = "日期")
    private String creatDate;

    @Excel(name = "号码")
    private String phone;

    @Excel(name = "宽带号码")
    private String broadbandNumber;

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "状态")
    private String state;

    @Excel(name = "套餐")
    private String packageName;

    @Excel(name = "发展人")
    private String develop;

    @Excel(name = "备注")
    private String note;

    @Excel(name = "分成月份")
    private String date;

    @Excel(name = "地址")
    private String address;

    @Excel(name = "套餐分成")
    private float amount1;

    @Excel(name = "宽带分成")
    private float amount2;

    @Excel(name = "端口费")
    private float amount3;

    @Excel(name = "总计")
    private float amount;

    private int creatUserId;

    private int type;

    private String policy;
}
