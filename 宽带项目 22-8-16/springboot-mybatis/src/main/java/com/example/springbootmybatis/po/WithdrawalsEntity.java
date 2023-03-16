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
@TableName("withdrawals")
public class WithdrawalsEntity implements Serializable {
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
	 * 0未支付，1已支付,2为已驳回
	 */
	private Integer status;

	private String billingImg;

	private String remark;

	private LocalDateTime dateTime;

	private LocalDateTime processDateTime;

	private int userId;

	private Double amount;
}
