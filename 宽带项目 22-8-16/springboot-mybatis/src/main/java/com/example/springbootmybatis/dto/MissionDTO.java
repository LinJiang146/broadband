package com.example.springbootmybatis.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class MissionDTO {


    private int id;

    private String customerName;

    private String phone;

    private double lat;

    private double lng;

    private String address;

    private String description;


    private Double amount;


    private String status;

    private int enable;

    private String  userName;

    private int isRoutine;

    private int type;
}
