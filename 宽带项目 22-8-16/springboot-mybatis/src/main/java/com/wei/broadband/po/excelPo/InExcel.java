package com.wei.broadband.po.excelPo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class InExcel {


    @Excel(name = "发展号码")
    private String fazhanNumber;

    @Excel(name = "金额（净额）")
    private float amount;

    @Excel(name = "费用计算月")
    private String date;

    @Excel(name = "政策名称（系统）")
    private String policyName;
}
