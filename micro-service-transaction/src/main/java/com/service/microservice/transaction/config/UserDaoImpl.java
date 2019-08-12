package com.service.microservice.transaction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author Simon
 * @Date 2019-08-12 15:52
 * @Describe
 */
@Repository
public class UserDaoImpl {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static String sql = "select * from user";

    public Map<String, Object> query(){
        return namedParameterJdbcTemplate.getJdbcTemplate().queryForMap(sql);
    }
}
