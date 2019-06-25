package com.simon.microservice.microserviceconsumerfeign.feign;

import com.google.gson.Gson;
import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import java.io.IOException;

/**
 * @author fengyue
 * @Date 2019-06-22 16:44
 */
public class RestApiCallUtils {

	public static <T> T getRestClient(Class<T> apiType, String url) {
		return Feign.builder()
                            .encoder(new GsonEncoder()) // 编码器
                            .decoder(new GsonDecoder()) // 解码器
                            .logger(new Logger.JavaLogger().appendToFile(System.getProperty("logpath") + "/http.log")) //日志信息
                            .logLevel(Logger.Level.FULL) //日志级别
                            .options(new Request.Options(1000, 1000)) //超时时间
                            .requestInterceptor(new RequestInterceptor() {
                                @Override
                                public void apply(RequestTemplate template) {
                                    //拦截器
                                }
                            })
                            .client(new Client() {
                                @Override
                                public Response execute(Request request, Request.Options options) throws IOException {
                                    //客户端组件
                                    return null;
                                }
                            })
                            .retryer(new Retryer() {
                                @Override
                                public void continueOrPropagate(RetryableException e) {
                                    //重试机制
                                }

                                @Override
                                public Retryer clone() {
                                    return null;
                                }
                            })
                            .target(apiType, url);
	}
}
