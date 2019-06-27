package com.simon.microservice.microserviceconsumerfeign.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.simon.microservice.microserviceconsumerfeign.interception.FeignBasicAuthRequestInterceptor;

import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * @author fengyue
 * @Date 2019-06-22 15:13
 */
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

//    @Bean
//    public Contract feignContract(){
//        return new feign.Contract.Default();
//    }

//    @Bean
//    public BasicAuthenticationInterceptor basicAuthenticationInterceptor(){
//        return new BasicAuthenticationInterceptor("user", "password");
//    }

//    @Bean
//    public FeignBasicAuthRequestInterceptor feignBasicAuthRequestInterceptor(){
//        return new FeignBasicAuthRequestInterceptor();
//    }

//    @Bean
//    public Request.Options options(){
//        return new Request.Options(50000,50000);
//    }
//
//    @Bean
//    public Decoder decoder(){
//        return new Decoder.Default();
//    }
//
//    @Bean
//    public Encoder encoder(){
//        return new Encoder.Default();
//    }


}
