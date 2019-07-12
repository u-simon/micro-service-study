package com.simon.microservice.microserviceconsumerfeign.interception;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author fengyue
 * @Date 2019-06-22 15:38
 */
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
//        template.header()
        System.out.println("FeignBasicAuthRequestInterceptor");
    }
}
