package com.example.springbootmybatis.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class Text implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;


    private String txtName;

    private String title;

    private String synopsis;


}
