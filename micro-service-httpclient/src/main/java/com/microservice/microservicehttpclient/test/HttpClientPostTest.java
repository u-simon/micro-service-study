package com.microservice.microservicehttpclient.test;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.microservice.microservicehttpclient.bean.User;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author simon
 * @Date 2019/10/11 14:29
 * @Describe
 */
public class HttpClientPostTest {

	public static void main(String[] args) throws Exception {
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		//
		// HttpPost httpPost = new HttpPost("https://www.oschina.net/");
		// httpPost.setHeader("user-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)
		// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
		//
		// List<NameValuePair> parameters = new ArrayList<>();
		//
		// parameters.add(new BasicNameValuePair("scope", "project"));
		// parameters.add(new BasicNameValuePair("q,", "java"));
		//
		// UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters,
		// "UTF-8");
		//
		// httpPost.setEntity(urlEncodedFormEntity);
		//
		// CloseableHttpResponse execute = httpClient.execute(httpPost);
		//
		// if (execute.getStatusLine().getStatusCode() == 200){
		// HttpEntity entity = execute.getEntity();
		// System.out.println(EntityUtils.toString(entity, "utf-8"));
		// }
		//
		// execute.close();
		// httpClient.close();

		// post请求 无参
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("http://localhost:12345/user/testPost");
		CloseableHttpResponse response = httpClient.execute(httpPost);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				System.out.println(EntityUtils.toString(entity, "UTF-8"));
			}
		}

		response.close();
		httpClient.close();


		// Post请求 普通参数
		httpClient = HttpClientBuilder.create().build();
		StringBuffer params = new StringBuffer();

		params.append("name=" + URLEncoder.encode("&", "utf-8")).append("&").append("age=24");

		httpPost = new HttpPost("http://localhost:12345/user/testPostParam?" + params);
		httpPost.setHeader("Content-type", "application/json; charset=utf-8");
		response = httpClient.execute(httpPost);

		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				System.out.println(EntityUtils.toString(entity, "UTF-8"));
			}
		}

		response.close();
		httpClient.close();


		// POST请求 对象参数
		httpClient = HttpClientBuilder.create().build();
		httpPost = new HttpPost("http://localhost:12345/user/testPostObject");
		User user = new User();
		user.setName("simon");
		user.setAge(12);
		user.setGender("男");
		StringEntity stringEntity = new StringEntity(JSON.toJSONString(user), "utf-8");

		httpPost.setEntity(stringEntity);

		httpPost.setHeader("Content-type", "application/json; charset=utf-8");

		response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				System.out.println(EntityUtils.toString(entity, "UTF-8"));
			}
		}
		
//		MultipartEntityBuilder.create().addPart("", new FileBody(new File(""))).addPart("1", new StringBody("", ContentType.APPLICATION_JSON)).build()
		response.close();
		httpClient.close();

		// POST 请求 对象参数+普通参数

		httpClient = HttpClientBuilder.create().build();

		List<NameValuePair> pairs = new LinkedList<>();
		pairs.add(new BasicNameValuePair("flag", "4"));
		pairs.add(new BasicNameValuePair("meaning", "这是什么鬼?"));
		URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(12345)
				.setPath("/user/testPostParamObject").setParameters(pairs).build();
		httpPost = new HttpPost(uri);

		stringEntity = new StringEntity(JSON.toJSONString(user), "utf-8");

		httpPost.setEntity(stringEntity);
		httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println(EntityUtils.toString(entity, StandardCharsets.UTF_8));
            }
        }
        response.close();
        httpClient.close();
	}
}
