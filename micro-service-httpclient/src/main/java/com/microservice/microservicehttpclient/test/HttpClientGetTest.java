package com.microservice.microservicehttpclient.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author simon
 * @Date 2019/10/11 14:23
 * @Describe
 */
public class HttpClientGetTest {

	public static void main(String[] args) throws IOException, URISyntaxException {
		// // 获得Http客户端
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		// // 创建Get请求
		// HttpGet httpGet = new HttpGet("http://www.baidu.com/s?wd=java");
		// // 发送请求
		// CloseableHttpResponse response = httpClient.execute(httpGet);
		// // 判断状态码
		// if (response.getStatusLine().getStatusCode() == 200) {
		// HttpEntity entity = response.getEntity();
		// String result = EntityUtils.toString(entity, "utf-8");
		// System.out.println(result);
		// }
		// // 关闭资源
		// response.close();
		// httpClient.close();

		// 1. get请求 无参
		// 获取http客户端(可以理解为: 你得现有一个浏览器，注意：实际上HttpClient与浏览器不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 创建Get请求
		HttpGet httpGet = new HttpGet("http://localhost:12345/user/testGet");
		// 由客户端执行(发送)get请求
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// 判断响应状态码
		if (response.getStatusLine().getStatusCode() == 200) {
			// 从响应模型中获取响应实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				System.out.println("响应内容长度为:" + entity.getContentLength());
				System.out.println("响应内容为:" + EntityUtils.toString(entity, "UTF-8"));
			}
		}

		if (response != null) {
			response.close();
		}
		if (httpClient != null) {
			httpClient.close();
		}


		// 2. get 请求 有参数(方式一)
		httpClient = HttpClientBuilder.create().build();
		StringBuffer params = new StringBuffer();
		params.append("name=" + URLEncoder.encode("li&", "Utf-8")).append("&").append("age=24");
		httpGet = new HttpGet("http://localhost:12345/user/testGetParam?" + params);
		// 配置信息
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) // 设置连接超时时间(ms)
				.setConnectionRequestTimeout(5000)// 设置请求超时时间(ms)
				.setSocketTimeout(5000)// socket读写超时时间(ms)
				.setRedirectsEnabled(false)// 设置是否允许重定向(默认为true)
				.build();
		// 将上面的配置信息，运用到这个get请求里
		httpGet.setConfig(requestConfig);
		response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		System.out.println("响应状态码为: " + response.getStatusLine());
		if (entity != null) {
			System.out.println("响应内容长度为:" + entity.getContentLength());
			System.out.println("响应内容为:" + EntityUtils.toString(entity, "UTF-8"));
		}

		if (response != null) {
			response.close();
		}
		if (httpClient != null) {
			httpClient.close();
		}

		// 3. get 请求 有参数(方式二)
		httpClient = HttpClientBuilder.create().build();

		// 将参数放入键值对NameValuePair中,再放入集合中
		List<NameValuePair> pairs = new LinkedList<>();
		pairs.add(new BasicNameValuePair("name", "&"));
		pairs.add(new BasicNameValuePair("age", "24"));

		// 设置uri信息，并将参数几个放入uri
		// 注：这里也支持一个键值对一个键值对地往里面放setParameter(String key, String value)
		URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(12345)
				.setPath("/user/testGetParam").setParameters(pairs).build();

		httpGet = new HttpGet(uri);

		requestConfig =
				RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000)
						.setSocketTimeout(5000).setRedirectsEnabled(false).build();
		httpGet.setConfig(requestConfig);
		response = httpClient.execute(httpGet);
		System.out.println("响应状态码为: " + response.getStatusLine());
		entity = response.getEntity();
		if (entity != null) {
			System.out.println("响应内容长度为:" + entity.getContentLength());
			System.out.println("响应内容为:" + EntityUtils.toString(entity, "UTF-8"));
		}

		if (response != null) {
			response.close();
		}
		if (httpClient != null) {
			httpClient.close();
		}

	}
}
