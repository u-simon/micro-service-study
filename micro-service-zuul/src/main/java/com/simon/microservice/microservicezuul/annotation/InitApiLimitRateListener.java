package com.simon.microservice.microservicezuul.annotation;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author Simon
 * @Date 2019-07-29 15:00
 * @Describe
 */
public class InitApiLimitRateListener implements ApplicationListener<ApplicationPreparedEvent> {
	private String controllerPath;

	public InitApiLimitRateListener(String controllerPath) {
		this.controllerPath = controllerPath;
	}

	@Override
	public void onApplicationEvent(ApplicationPreparedEvent event) {
		try {
			initLimitRateApi();
		} catch (Exception e) {
			throw new RuntimeException("初始化需要进行并发限制的api异常", e);
		}
	}

	private void initLimitRateApi() throws ClassNotFoundException {
		Object rate = System.getProperty("open.api.defaultLimit") == null ? 100
				: System.getProperty("open.api.defaultLimit");
		ApiLimitAspect.semaphoreMap.put("open.api.defaultLimit",
				new Semaphore(Integer.parseInt((String) rate)));

		List<String> classList = null;

		for (String clazz : classList) {
			Class<?> clz = Class.forName(clazz);
			if (!clz.isAnnotationPresent(RestController.class)) {
				continue;
			}
			Method[] declaredMethods = clz.getDeclaredMethods();
			for (Method method : declaredMethods) {
				if (method.isAnnotationPresent(ApiRateLimit.class)) {
					String confKey = method.getAnnotation(ApiRateLimit.class).confKey();
					if (System.getProperty(confKey) != null) {
						ApiLimitAspect.semaphoreMap.put(confKey,
								new Semaphore(Integer.parseInt(System.getProperty(confKey))));
					}
				}
			}
		}
	}
}
