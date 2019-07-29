package com.simon.microservice.microservicezipkin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MicroServiceZipkinApplicationTests {

	@Test
	public void contextLoads() {
//		Map<Integer, String> ma = new HashMap<>();
//		ma.put(0, "123");
//		System.out.println(ma.get(1));

		int a = 79810;
		double b= a;

		System.out.println(Math.ceil( b/ 3));
	}

}
