package com.example.springbootmybatis.po;

import lombok.Data;

@Data
public class MissionForm {
    private String name;
    private String phone;
    private String address;
    private String broadbandNumber;
    private Double lat;
    private Double lng;
    private String imgName;
    private String failureType;
    private String description;
    private String subCard;
    private int type;

    private int missionId;

}
