package com.wei.broadband.dto;

import com.wei.broadband.po.BroadbandCustomer;
import com.wei.broadband.po.Maintain;
import lombok.Data;

@Data
public class BroadbandAndMaintain {

    public BroadbandCustomer broadbandCustomer;

    public Maintain maintain;
}
