package com.example.springbootmybatis.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author wei
 * @email 
 * @date 2023-02-19 14:07:31
 */
@Data
@TableName("payments")
public class PaymentsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private int id;
	/**
	 * 
	 */
	private String type;
	/**
	 *
	 */
	private String phone;
	/**
	 * 
	 */
	private LocalDateTime dateTime;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	private Double amount;


	private Integer userId;


	private String userName;

	private int status;
}
