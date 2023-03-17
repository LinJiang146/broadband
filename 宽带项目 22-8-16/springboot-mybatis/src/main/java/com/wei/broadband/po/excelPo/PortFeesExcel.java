package com.wei.broadband.po.excelPo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class PortFeesExcel {
    @Excel(name = "号码")
    private String broadNumber;

    @Excel(name = "结算金额")
    private float portFee;

    @Excel(name = "日期")
    private String date;
}
