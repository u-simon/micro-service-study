package com.service.microservice.transaction.onlyfenbiao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import javax.sql.DataSource;

import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.google.common.collect.Maps;

/**
 * @author Simon
 * @Date 2019-08-14 10:50
 * @Describe
 */
//@Component
//@Configuration
public class DatasourceConfig {

	@Value("${database0.url}")
	private String url;
	@Value("${database0.username}")
	private String username;
	@Value("${database0.password}")
	private String password;
	@Value("${database0.driverClassName}")
	private String driverClassName;
	@Value("${database0.databaseName}")
	private String databaseName;

//	@Bean
	public DataSource dataSource() throws SQLException {
		return buildDatasource();

	}

	private DataSource buildDatasource() throws SQLException {
		Map<String, DataSource> dataSourceMap = Maps.newHashMapWithExpectedSize(2);
		dataSourceMap.put(this.databaseName, createDataSource(databaseName));
		// 设置默认db为ds_0，也就是为那些没有配置分库分表策略的指定的默认库
		// 如果只有一个库，也就是不需要分库的话，map里只放一个映射就行了，只有一个库时不需要指定默认库，
		// 但2个及以上时必须指定默认库，否则那些没有配置策略的表将无法操作数据
		DataSourceRule rule = new DataSourceRule(dataSourceMap, databaseName);
		// 2.设置分表映射，将t_order_0和t_order_1两个实际的表映射到t_order逻辑表
		TableRule tableRule = TableRule.builder("user")
				.actualTables(Arrays.asList("user_0", "user_1", "user_2", "user_3"))
				.dataSourceRule(rule).build();
		// 3.具体的分库分表策略
		ShardingRule shardingRule =
				ShardingRule.builder().dataSourceRule(rule).tableRules(Arrays.asList(tableRule))
						.tableShardingStrategy(
								new TableShardingStrategy("id", new DatabaseShardingAlgorithm()))
						.build();
		// 创建数据源
		return ShardingDataSourceFactory.createDataSource(shardingRule);
	}

	private DataSource createDataSource(String dataSourceName) {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(this.driverClassName);
		dataSource.setUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		return dataSource;
	}

//	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws SQLException {
		return new NamedParameterJdbcTemplate(dataSource());
	}

//	@Bean
	public JdbcTemplate jdbcTemplate() throws SQLException {
		return new JdbcTemplate(dataSource());
	}
}
