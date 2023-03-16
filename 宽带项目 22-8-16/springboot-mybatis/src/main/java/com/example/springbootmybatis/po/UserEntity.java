package com.example.springbootmybatis.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;


public class UserEntity {
    @Excel(name = "姓名")
    private String name;

    @Excel(name = "年龄")
    private int age;

    @Excel(name = "操作时间",format="yyyy-MM-dd HH:mm:ss", width = 20.0)
    private Date time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }



}
