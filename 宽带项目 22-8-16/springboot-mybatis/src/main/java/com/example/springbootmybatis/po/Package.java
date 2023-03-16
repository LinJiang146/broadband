package com.example.springbootmybatis.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Package implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    //名字
    private String name;

    //套餐详细
    private String description;

    //价格
    private int price;

    //套餐类型
    private int type;

    private float commission;





    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private int createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private int updateUser;

}
