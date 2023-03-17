package com.wei.broadband.po;

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
 * @date 2023-03-04 16:45:36
 */
@Data
@TableName("sub_card")
public class SubCardEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private int id;
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

	private int isCarrier;

	private int cusId;
}
