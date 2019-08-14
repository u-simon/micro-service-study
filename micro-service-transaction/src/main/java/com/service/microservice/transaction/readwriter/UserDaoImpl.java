package com.service.microservice.transaction.readwriter;

import com.dangdang.ddframe.rdb.sharding.api.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public List<User> query(){
        HintManager.getInstance().setMasterRouteOnly(); //Hint强制路由主库
        return namedParameterJdbcTemplate.getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    private static String save = " insert into user (city, name) values (:city, :name)";
    public void save(Map<String, Object>  param){
        namedParameterJdbcTemplate.update(save, param);
    }
}
