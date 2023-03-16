package com.example.springbootmybatis.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author wei
 * @email 
 * @date 2023-02-22 20:06:31
 */
@Data
@TableName("mission")
public class MissionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private int id;
	/**
	 * 
	 */
	private Integer customerId;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	private Double amount;
	/**
	 * 
	 */
	private String status;
	/**
	 * 
	 */
	private Integer userId;

	@TableField(exist = false)
	private Integer walletItemId;

	private int enable;

	private int type;

	private int packageId;

	private int isRoutine;

}
