package com.service.microservice.transaction.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author Simon
 * @Date 2019-08-12 15:42
 * @Describe
 */
@Data
@Component
@ConfigurationProperties(prefix = "database1")
public class Database1Config {
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	private String databaseName;

	public DataSource createDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driverClassName);
		return dataSource;
	}
}
