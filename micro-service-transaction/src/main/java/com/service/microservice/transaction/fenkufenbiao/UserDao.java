package com.service.microservice.transaction.fenkufenbiao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Simon
 * @Date 2019-08-20 09:56
 * @Describe 清风拂袖揽明月, 皓月鉴怀渡银河
 */
@Repository
public class UserDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static String insert = " insert into user (id,`name`, city) values (:id,:name, :city)";

    public void insert(User user){
        namedParameterJdbcTemplate.update(insert, new BeanPropertySqlParameterSource(user));
    }
}
