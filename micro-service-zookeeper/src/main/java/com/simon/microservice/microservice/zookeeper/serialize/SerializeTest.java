package com.simon.microservice.microservice.zookeeper.serialize;

import org.apache.jute.BinaryInputArchive;
import org.apache.jute.BinaryOutputArchive;
import org.apache.zookeeper.server.ByteBufferInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author simon
 * @Date 2019/11/9 16:42
 * @Describe 一念花开, 一念花落
 */
public class SerializeTest {

    /**
     * 使用jute对MockReqHeader对象进行序列化和反序列化
     *      1.实体类需要实现Record接口的serialize和deserialize方法
     *      2.构建一个序列化器BinaryOutputArchive
     *      3.序列化
     *          -> 调用实体类的serialize方法,将对象序列化到tag中去 例如在本例中就将MockReqHeader对象序列化到header中去
     *      4.反序列化
     *          -> 调用实体类的deserialize,从指定的tag中反序列化出数据内容
     */


    public static void main(String[] args) throws IOException {
        //开始序列化
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryOutputArchive archive = BinaryOutputArchive.getArchive(byteArrayOutputStream);
        new MockReqHeader(0x2324, "ping").serialize(archive, "header");
        ByteBuffer wrap = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(wrap);

        //开始反序列化
        BinaryInputArchive bbia = BinaryInputArchive.getArchive(byteBufferInputStream);
        MockReqHeader mockReqHeader = new MockReqHeader();
        mockReqHeader.deserialize(bbia, "header");

        byteBufferInputStream.close();
        byteArrayOutputStream.close();
    }
}
