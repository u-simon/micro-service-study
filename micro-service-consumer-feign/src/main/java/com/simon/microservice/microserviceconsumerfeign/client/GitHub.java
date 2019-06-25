package com.simon.microservice.microserviceconsumerfeign.client;

import java.util.List;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author fengyue
 * @Date 2019-06-22 16:14
 */
public interface GitHub {
    @RequestLine("GET /repos/{owner}/{repo}")
    @Headers("Content-Type: application/json")
    List<Object> getRequest(@Param("owner") String owner, @Param("repo") String repo);

    @RequestLine("POST /account/{id}")
    @Headers("Content-Type: application/json")
    @Body("%7B\"user_name\":\"{username}\", \"password\":\"{password}\"")
    List<Object> postRequest(@Param("id") String id);
}
