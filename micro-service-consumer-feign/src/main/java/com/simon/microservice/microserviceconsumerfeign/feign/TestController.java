package com.simon.microservice.microserviceconsumerfeign.feign;

import feign.Feign;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengyue
 * @Date 2019-06-22 16:47
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/callHello")
    public String callHello(){
        FeignRemote target =RestApiCallUtils.getRestClient(FeignRemote.class, "http://localhost:8001");
        return target.hello();
    }
}
