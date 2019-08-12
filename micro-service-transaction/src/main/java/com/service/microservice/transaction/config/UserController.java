package com.service.microservice.transaction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
