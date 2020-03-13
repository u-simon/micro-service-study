package com.microservice.microservicehttpclient.base;

import com.google.common.collect.Lists;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.URI;
import java.util.List;

/**
 * @author simon
 * @date 2020/3/5 16:38
 * @describe 一念花开, 一念花落
 */
public class SimpleCase {


	public static void main(String[] args) throws Exception {
		HttpClient client = new DefaultHttpClient();

		/**
		 * httpGet httpPost httpPut httpDelete httpTrace httpOptions httpHead
		 */
		HttpGet httpGet = new HttpGet("https://www.baidu.com");

		HttpResponse execute = client.execute(httpGet);

		HttpEntity entity = execute.getEntity();
		if (entity != null) {
			InputStream content = entity.getContent();
			byte[] data = new byte[1024];
			int length = -1;
			while ((length = content.read(data)) != -1) {
				System.out.println(length);
			}
		}


		/**
		 * 请求 URI 是统一资源定位符,它标识了应用于那个请求之上的资源
		 */

		URI uri = URIUtils.createURI("https", "www.google.com", -1, "/search",
				"q=httpclient&btnG=Google+Search&oq=", null);
		System.out.println(new HttpGet(uri).getURI());

		/**
		 * 查询字符串独立出来
		 */

		List<NameValuePair> params = Lists.newArrayList();

		/**
		 * NameValuePair 查询字符串的k-v
		 */
		params.add(new BasicNameValuePair("q", "httpclient"));
		params.add(new BasicNameValuePair("btnG", "Google+Search"));
		params.add(new BasicNameValuePair("aq", "f"));
		params.add(new BasicNameValuePair("oq", null));
		URI http = URIUtils.createURI("http", "www.google.com", -1, "/search",
				URLEncodedUtils.format(params, "utf-8"), null);
		System.out.println(new HttpGet(http).getURI());

		/**
		 * Http响应是由服务器在接收和解释请求报文之后返回发送给客户端的报文 响应报文的第一行包含了协议版本,之后是数字状态码和相关联的文本段
		 */

		BasicHttpResponse response =
				new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

		response.addHeader("Set-Cookie", "c1=\"a\"; path=\"/\"; domain=\"location\"; ");
		response.addHeader("Set-Cookie", "c2=\"b\"; path=\"/\"; c3=c; domain=\"location\"");

		// 版本号
		System.out.println(response.getProtocolVersion());
		// 状态码
		System.out.println(response.getStatusLine().getStatusCode());
		// reason
		System.out.println(response.getStatusLine().getReasonPhrase());
		System.out.println(response.getStatusLine().toString());


		// 获取所有的响应头
		HeaderIterator headerIterator = response.headerIterator();
		while (headerIterator.hasNext()) {
			System.out.println(headerIterator.next());
		}
		System.out.println("---------------BasicHeaderElementIterator------------------");

		BasicHeaderElementIterator elementIterator =
				new BasicHeaderElementIterator(response.headerIterator());
		// 解析Http报文到独立头部信息元素
		while (elementIterator.hasNext()) {
			HeaderElement element = elementIterator.nextElement();

			//
			System.out.println(element.getName() + " = " + element.getValue());

			NameValuePair[] parameters = element.getParameters();
			for (NameValuePair parameter : parameters) {
				System.out.println(parameter);
			}
		}


		System.out.println("---------------Entity------------------");

		/**
		 * Http报文可以携带和请求或相应相关的内容实体,实体可以在一些请求和响应中找到,因为他们也是可选的,使用了实体的请求被称为封闭实体请求
		 * Http规范定义了两种封闭实体的方法: post和put,相应通常期望包含一个内容实体
		 *
		 * HttpClient根据其内容出自何处区分三种类型的实体
		 * -> streamed流式:内容从流中获得,或者在运行中产生,特别是这种分类包含从Http响应中获取的实体,流式实体是不可重复生成的
		 * -> self-contained 自我包含式 :内容在内存中或通过独立的连接或其他实体中获得,自我包含式的实体是可以重复生成的,这种类型的实体会经常用于封闭http请求的实体
		 * -> wrapping 包装式 ：内容从另外一个实体中获得
		 */

		StringEntity stringEntity = new StringEntity("import message", "utf-8");
		System.out.println(stringEntity.getContentType());
		System.out.println(stringEntity.getContentLength());
		System.out.println(EntityUtils.getContentCharSet(stringEntity));
		System.out.println(EntityUtils.toString(stringEntity));
		System.out.println(EntityUtils.toByteArray(stringEntity).length);

		System.out.println("---------------动态内容实体------------------");

		/**
		 * Http实体需要基于特定的执行上下文来动态地生成，通过使用EntityTemplate实体类和ContentProducer接口,HttpClient提供了动态实体的支持.
		 * 内容生成器是按照需求生成他们内容的对象,将他们写入到一个输出流中,他们还每次被请求时来生成内容
		 */

		HttpPost httpPost = new HttpPost();
		httpPost.setEntity(new EntityTemplate(new ContentProducer() {
			@Override
			public void writeTo(OutputStream outStream) throws IOException {
				Writer writer = new OutputStreamWriter(outStream, "utf-8");
				writer.write("<response>");
				writer.write("<content>");
				writer.write(" import stuff ");
				writer.write("</content");
				writer.write("</response>");
			}
		}));

		System.out.println("---------------HTML表单------------------");

		List<NameValuePair> formParams = Lists.newArrayList();
		formParams.add(new BasicNameValuePair("param1", "value1"));
		formParams.add(new BasicNameValuePair("param2", "value2"));
		httpPost.setEntity(new UrlEncodedFormEntity(formParams, "utf-8"));


		System.out.println("---------------请求重试------------------");

		new DefaultHttpClient().setHttpRequestRetryHandler(new HttpRequestRetryHandler(){
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 5){
					return false;
				}

				if (exception instanceof NoHttpResponseException){
					return false;
				}

				if (exception instanceof SSLHandshakeException){
					return false;
				}

				HttpRequest  attribute = (HttpRequest)
						context.getAttribute(ExecutionContext.HTTP_REQUEST);
				if (!(attribute instanceof HttpEntityEnclosingRequest)){
					return true;
				}
				return false;
			}
		});

	}
}
