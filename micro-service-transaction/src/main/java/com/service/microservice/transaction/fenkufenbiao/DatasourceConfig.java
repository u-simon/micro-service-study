package com.service.microservice.transaction.fenkufenbiao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.google.common.collect.Maps;

/**
 * @author Simon
 * @Date 2019-08-20 09:59
 * @Describe 清风拂袖揽明月, 皓月鉴怀渡银河
 */
@Configuration
public class DatasourceConfig {

    @Value("${database0.url}")
    private String url0;

    @Value("${database0.username}")
    private String username0;

    @Value("${database0.password}")
    private String password0;

    @Value("${database0.driverClassName}")
    private String driverClass0;

    @Value("${database0.databaseName}")
    private String databaseName0;

    public DataSource createDatesource0(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url0);
        druidDataSource.setUsername(username0);
        druidDataSource.setPassword(password0);
        druidDataSource.setDriverClassName(driverClass0);
        return druidDataSource;
    }

    @Value("${database1.url}")
    private String url1;

    @Value("${database1.username}")
    private String username1;

    @Value("${database1.password}")
    private String password1;

    @Value("${database1.dirverClassName}")
    private String driverClass1;

    @Value("${database1.databaseName}")
    private String databaseName1;

    public DataSource createDatesource1(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url1);
        druidDataSource.setUsername(username1);
        druidDataSource.setPassword(password1);
        druidDataSource.setDriverClassName(driverClass1);
        return druidDataSource;
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        return buildDataSource();
    }

    private DataSource buildDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = Maps.newHashMapWithExpectedSize(2);
        dataSourceMap.put(this.databaseName0, createDatesource0());
        dataSourceMap.put(this.databaseName1, createDatesource1());
        // 设置默认db为ds_0，也就是为那些没有配置分库分表策略的指定的默认库
        // 如果只有一个库，也就是不需要分库的话，map里只放一个映射就行了，只有一个库时不需要指定默认库，
        // 但2个及以上时必须指定默认库，否则那些没有配置策略的表将无法操作数据
        DataSourceRule rule = new DataSourceRule(dataSourceMap, databaseName0);
        // 2.设置分表映射，将t_order_0和t_order_1两个实际的表映射到t_order逻辑表
        TableRule tableRule = TableRule.builder("user")
                .actualTables(Arrays.asList("user_0", "user_1"))
                .dataSourceRule(rule).build();
        // 3.具体的分库分表策略
        ShardingRule shardingRule =
                ShardingRule.builder().dataSourceRule(rule).tableRules(Arrays.asList(tableRule))
                        .tableShardingStrategy(
                                new TableShardingStrategy("id", new TableShardingAlgorithm()))
                        .databaseShardingStrategy(new DatabaseShardingStrategy("city", new DatasourceShardingAlgorithm()))
                        .build();
        // 创建数据源
        return ShardingDataSourceFactory.createDataSource(shardingRule);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws SQLException {
        return  new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws SQLException {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }
}
