package com.wei.broadband.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

   private int id;

   //名字
   private String name;

   //账户
   private String username;


   //密码
   private String password;

   //手机号码
   private String phone;

   //状态，0封禁，1启用
   private int status;

   private Double balance;

   //权限,0为最高权限，1为高级管理员，2为普通管理员
   private int permission;

   private int revenueStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private int createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private int updateUser;

    private String role;
}
