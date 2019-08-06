package com.simon.microservice.job.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Simon
 * @Date 2019-08-06 11:09
 * @Describe Failure is not an antonym of success, mediocrity is
 */
public class MySimpleJob implements SimpleJob {
	@Override
	public void execute(ShardingContext shardingContext) {

		String shardingParameter = shardingContext.getShardingParameter();
		System.out.println("分片参数：" + shardingParameter);

		int a = 1 / 0;
		int value = Integer.parseInt(shardingParameter);
		for (int i = 0; i < 10; i++) {
			if (i % 2 == value) {
                String format = new SimpleDateFormat("HH:mm:ss").format(new Date());
                System.out.println(format + ": 开始执行简单任务" + i);
			}
		}
	}
}
