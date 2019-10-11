package com.microservice.microservicehttpclient.controller;

import com.microservice.microservicehttpclient.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author simon
 * @Date 2019/10/11 14:56
 * @Describe
 */
@RestController
@RequestMapping("/user")
public class UserController {


	@RequestMapping("/testGet")
	public Map<String, Object> testGet() {
		Map<String, Object> response = new HashMap<>(1);
		response.put("result", "123");
		return response;
	}

	@RequestMapping("/testGetParam")
	public Map<String, Object> testGetParam(@RequestParam("name") String name,
			@RequestParam("age") Integer age) {
		Map<String, Object> response = new HashMap<>(1);
		response.put("result", "没想到[" + name + "]都" + age + "岁了!");
		return response;
	}

	@RequestMapping(value = "/testPost", method = RequestMethod.POST)
	public Map<String, Object> testPost() {
		Map<String, Object> response = new HashMap<>(1);
		response.put("result", "123");
		return response;
	}

	@RequestMapping(value = "/testPostParam", method = RequestMethod.POST)
	public Map<String, Object> testPostParam(String name, Integer age) {
		Map<String, Object> response = new HashMap<>(1);
		response.put("result", "没想到[" + name + "]都" + age + "岁了!");
		return response;
	}

	@RequestMapping(value = "/testPostObject", method = RequestMethod.POST)
	public Map<String, Object> testPostObject(@RequestBody User user) {
		System.out.println(user);
		Map<String, Object> response = new HashMap<>(1);
		response.put("result", user.toString());
		return response;
	}

	@RequestMapping(value = "/testPostParamObject", method = RequestMethod.POST)
	public Map<String, Object> testPostParamObject(@RequestBody User user, Integer flag,
			String meaning) {
		Map<String, Object> response = new HashMap<>(1);
		response.put("result", user.toString() + "\n" + flag + "\n" + meaning);
		return response;
	}

}
