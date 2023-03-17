package com.wei.broadband.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author wei
 * @email 
 * @date 2023-02-05 12:45:32
 */
@Data
public class CustomerBilling implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private int id;
	/**
	 * 
	 */
	private String phone;
	/**
	 * 
	 */
	private String date;
	/**
	 * 
	 */
	private String status;
	/**
	 * 
	 */
	private float phoneBills;
	/**
	 * 
	 */
	private float balance;

	private float cost;

	private float arrears;

	private float grants;
}
