package com.wei.broadband.po;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Revenue {
    private int id;

    private float amount;

    private String description;

    private int type;

    private int userId;

    private LocalDate date;
}
