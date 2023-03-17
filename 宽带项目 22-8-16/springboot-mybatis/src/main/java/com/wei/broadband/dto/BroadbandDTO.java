package com.wei.broadband.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BroadbandDTO {

    private int id;

    private String name;

    private String phone;

    private String alternatePhone;

    private String address;

//    private int packageId;
    private String packageName;

    private int contract;

    private String broadbandNumber;

    private String image;

    private String position;

//    private int type;
    private String type;

    private int commissioningFee;

    private int predepositFee;

    private int lightcatFee;

    private int settopboxFee;

    private int isMonitoring;

    private int monitoringFee;

    private double lat;

    private double lng;

//    private int status;
    private String status;

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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

//    private int createUser;
    private String createUser;

    private int updateUser;

}
