package com.example.springbootmybatis.dto;

import com.example.springbootmybatis.po.BroadbandCustomer;
import com.example.springbootmybatis.po.Maintain;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BroadbandAndMaintain {

    public BroadbandCustomer broadbandCustomer;

    public Maintain maintain;
}
