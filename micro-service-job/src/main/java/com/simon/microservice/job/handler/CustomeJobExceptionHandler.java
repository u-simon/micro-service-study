package com.simon.microservice.job.handler;

import com.dangdang.ddframe.job.executor.handler.JobExceptionHandler;

/**
 * 异常处理
 * 
 * @author Simon
 * @Date 2019-08-06 16:15
 * @Describe Failure is not an antonym of success, mediocrity is
 */
public class CustomeJobExceptionHandler implements JobExceptionHandler {
	@Override
	public void handleException(String s, Throwable throwable) {
		System.out.println(s + "跑出异常" + throwable);
	}
}
