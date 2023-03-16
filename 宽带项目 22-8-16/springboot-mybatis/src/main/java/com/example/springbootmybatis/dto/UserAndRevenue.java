package com.example.springbootmybatis.dto;

import com.example.springbootmybatis.po.Revenue;
import com.example.springbootmybatis.po.User;
import lombok.Data;

@Data
public class UserAndRevenue {
    public User user;

    public Revenue revenue;
}
