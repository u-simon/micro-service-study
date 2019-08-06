package com.simon.microservice.job.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

import java.util.Arrays;
import java.util.List;

/**
 * @author Simon
 * @Date 2019-08-06 12:01
 * @Describe Failure is not an antonym of success, mediocrity is
 */
public class MyDataflowJob implements DataflowJob<String> {
	@Override
	public List<String> fetchData(ShardingContext shardingContext) {
		return Arrays.asList("1", "2", "3");
	}

	@Override
	public void processData(ShardingContext shardingContext, List<String> list) {
		System.out.println("处理数据:" + list.toString());

	}
}
