package com.example.springbootmybatis.po;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Maintain {
    private int id;

    private String image;

    private int customerId;

    private LocalDate date;

    private int userId;

    private String failureType;

    private String description;

    private int type;
}
