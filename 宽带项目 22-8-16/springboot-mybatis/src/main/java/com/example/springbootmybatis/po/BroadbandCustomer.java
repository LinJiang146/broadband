package com.example.springbootmybatis.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BroadbandCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String phone;

    private String alternatePhone;

    private String address;

    private int packageId;

    private int contract;

    private String broadbandNumber;

    private String image;

    private String position;

    private int type;

    private int commissioningFee;

    private int predepositFee;

    private int lightcatFee;

    private int settopboxFee;

    private int isMonitoring;

    private int monitoringFee;

    private double lat;

    private double lng;

    private int status;

    private int iptv;

    private LocalDateTime iptvDate;

    private float balance;

    private String phone1;

    private LocalDate date1;

    private String phone2;

    private LocalDate date2;

    private String phone3;

    private LocalDate date3;

    private String phone4;

    private LocalDate date4;

    private String phone5;

    private LocalDate date5;

    private String isxz;

    private String township;

    private String village;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private int createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private int updateUser;
}
