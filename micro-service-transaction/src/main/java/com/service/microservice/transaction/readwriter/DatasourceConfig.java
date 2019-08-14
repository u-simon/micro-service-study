package com.service.microservice.transaction.readwriter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.dangdang.ddframe.rdb.sharding.api.MasterSlaveDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.strategy.slave.MasterSlaveLoadBalanceStrategyType;

/**
 * @author Simon
 * @Date 2019-08-12 15:31
 * @Describe
 */
//@Configuration
public class DatasourceConfig {

	@Autowired
	Database0Config database0Config;

	@Autowired
	Database1Config database1Config;

	@Bean
	public DataSource dataSource() throws SQLException {
		return buildDataSource();
	}

	private DataSource buildDataSource() throws SQLException {
		Map<String, DataSource> slaveDatasource = new HashMap<>();
		slaveDatasource.put(database0Config.getDatabaseName(), database0Config.createDataSource());
        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource("masterSlave",
                database1Config.getDatabaseName(), database1Config.createDataSource(),
                slaveDatasource, MasterSlaveLoadBalanceStrategyType.getDefaultStrategyType());
        return dataSource;
	}

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws SQLException {
	    return  new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws SQLException {
	    return new JdbcTemplate(dataSource());
    }
}
