package com.service.microservice.transaction.config;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author Simon
 * @Date 2019-08-12 15:55
 * @Describe
 */
@Controller
public class UserController {

    @Autowired
    UserDaoImpl userDao;

    @RequestMapping("/query")
    @ResponseBody
    public Object query(){
       return userDao.query();
    }

    @RequestMapping("/save")
    @ResponseBody
    public void save(){
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
        params.put("city", "天津");
        params.put("name", "amy");
        userDao.save(params);
    }
}
