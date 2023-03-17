package com.wei.broadband.dto;

import com.wei.broadband.po.Revenue;
import com.wei.broadband.po.User;
import lombok.Data;

@Data
public class UserAndRevenue {
    public User user;

    public Revenue revenue;
}
