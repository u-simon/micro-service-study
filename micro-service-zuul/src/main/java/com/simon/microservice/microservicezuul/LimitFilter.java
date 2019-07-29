package com.simon.microservice.microservicezuul;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon
 * @Date 2019-07-29 10:57
 */

public class LimitFilter extends ZuulFilter {

	public static volatile RateLimiter rateLimiter = RateLimiter.create(100.0);

	@Resource(name = "longRedisTemplate")
	RedisTemplate<String, Long> redisTemplate;

	public LimitFilter() {
		super();
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext currentContext = RequestContext.getCurrentContext();

		// Object serviceId = currentContext.get("serviceId"); // 获取Eureka中注册的服务名称
		String key = "rate_limit_" + System.currentTimeMillis() / 1000;
		try {
			if (!redisTemplate.hasKey(key)) {
				redisTemplate.opsForValue().set(key, 0L, 100, TimeUnit.SECONDS);
			}

			int rate = Integer.parseInt(System.getProperty("api.clusterLimitRate"));
			if (redisTemplate.opsForValue().increment(key, 1) > rate) {
				currentContext.setSendZuulResponse(false); // false 表示这个请求最终不会被zuul转发后端服务器
				currentContext.set("isSuccess", false);
				Map<String, Object> result = Maps.newHashMap();
				result.put("msg", "当前负载太高，请售后重试");
				result.put("code", "403");
				currentContext.setResponseBody(JSON.toJSONString(result));
				currentContext.getResponse().setContentType("application/json; charset=utf-8");
			}
		} catch (Exception e) {
			rateLimiter.acquire();
		}
		return null;
	}
}
