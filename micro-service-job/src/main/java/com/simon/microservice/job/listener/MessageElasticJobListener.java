package com.simon.microservice.job.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;

/**
 * 任务执行监听器
 * 
 * @author Simon
 * @Date 2019-08-06 16:09
 * @Describe Failure is not an antonym of success, mediocrity is
 */
public class MessageElasticJobListener implements ElasticJobListener {
	@Override
	public void beforeJobExecuted(ShardingContexts shardingContexts) {
		System.out.println(shardingContexts.getJobName() + "任务开始执行 =====  "
				+ new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}

	@Override
	public void afterJobExecuted(ShardingContexts shardingContexts) {
		System.out.println(shardingContexts.getJobName() + "任务执行结束 =====  "
				+ new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}
}
