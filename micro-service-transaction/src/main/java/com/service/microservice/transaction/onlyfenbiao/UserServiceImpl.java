package com.service.microservice.transaction.onlyfenbiao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Simon
 * @Date 2019-08-14 11:44
 * @Describe
 */
@Service
public class UserServiceImpl {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private String Save = "insert into user (id,`name`) values (:id,:name)";
    public void save(Map<String, Object> params){
        namedParameterJdbcTemplate.update(Save, params);
    }
}
