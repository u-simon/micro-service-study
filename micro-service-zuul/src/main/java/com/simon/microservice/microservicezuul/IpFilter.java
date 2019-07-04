package com.simon.microservice.microservicezuul;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.simon.microservice.microservicezuul.utils.IPUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @author fengyue
 * @Date 2019-07-04 11:34
 */
public class IpFilter extends ZuulFilter {

    private Set<String> IpBlackList =  new HashSet<>();
	{
		IpBlackList.add("127.0.0.1");
	}

	public IpFilter() {
		super();
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		System.out.println(2 / 0);
		RequestContext currentContext = RequestContext.getCurrentContext();
		HttpServletRequest request = currentContext.getRequest();
		String ip = IPUtils.getRemoteHost(request);
		System.out.println(ip);
		if (StringUtils.isNotBlank(ip) && IpBlackList.contains(ip)) {
            currentContext.setSendZuulResponse(false);
			Map<String, Object> data = new HashMap<>();
			data.put("code", 403);
			data.put("msg", "非法请求");
            currentContext.setResponseBody(JSON.toJSONString(data));
            currentContext.getResponse().setContentType("application/json; charset=utf-8");
            return null;
		}
		return null;
	}
}
