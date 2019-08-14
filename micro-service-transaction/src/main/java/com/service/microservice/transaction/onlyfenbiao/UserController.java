package com.service.microservice.transaction.onlyfenbiao;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author Simon
 * @Date 2019-08-14 11:47
 * @Describe
 */
@Controller
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping("/save")
    @ResponseBody
    public void save(){
        for (int i = 0; i < 100; i++){
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
            params.put("id", (long)i + 1);
            params.put("name",  i + 1 + "");
            userService.save(params);
        }
    }
}
