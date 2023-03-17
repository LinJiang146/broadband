package com.wei.broadband.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author wei
 * @email 
 * @date 2023-02-19 14:07:31
 */
@Data
@TableName("mission_item")
public class MissionItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private int id;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	private Double amount;


	private String updateTime;


	private  int packageId;

	private int type;

	private int enable;
}
