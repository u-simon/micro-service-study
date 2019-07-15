package com.simon.microservice.microserviceadmin.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;

/**
 * @author Simon
 * @Date 2019-07-12 18:01
 */
public class DingDingMessageUtil {

	public static String access_token = "";

	public static void sendTextMessage(String msg) {
		try {
            Message message = new Message();
            message.setMsgType("text");
            message.setText(new MessageInfo(msg));

            URL url = new URL("https://oapi.dingtalk.com/robot/send?access_token=" + access_token);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-type", "application/Json; charset=UTF-8");
            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            String textMessage = JSON.toJSONString(message);
            byte[] data = textMessage.getBytes();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();

            InputStream inputStream = connection.getInputStream();
            byte[] in = new byte[inputStream.available()];
            inputStream.read(in);

            System.out.println(new String(in));

        } catch (Exception e) {

		}
	}
}


class Message {
	private String msgType;
	private MessageInfo text;

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public MessageInfo getText() {
		return text;
	}

	public void setText(MessageInfo text) {
		this.text = text;
	}
}


class MessageInfo {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageInfo(String content) {
        this.content = content;
    }
}
