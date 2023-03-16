package com.example.springbootmybatis.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CardCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String phone;

    private String alternatePhone;

    private String address;

    private int packageId;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private int createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private int updateUser;

}
