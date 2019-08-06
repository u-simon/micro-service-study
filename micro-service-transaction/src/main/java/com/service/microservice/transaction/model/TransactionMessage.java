package com.service.microservice.transaction.model;

import java.util.Date;

import com.service.microservice.transaction.enums.TransactionMessageStatusEnum;

import lombok.Data;

/**
 * @author Simon
 * @Date 2019-08-03 14:33
 * @Describe
 */
@Data
public class TransactionMessage {
	private Long id; // 消息ID

	private String message; // 消息内容

	private String queue; // 队列名称

	private String sendSystem; // 发送消息的系统

	private int sendCount; // 重复发送的次数

	private Date createDate; // 创建时间

	private Date sendDate; // 最近发送消息时间

	private int status = TransactionMessageStatusEnum.WATING.getStatus(); // 状态

	private int dieCount = 10; // 死亡次数

	private Date customerDate; // 消费时间

	private String customerSystem; // 消费系统

	private Date dieDate;// 死亡时间

}
