package org.example.socket;

import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 5678);

        // 向服务器发消息
        OutputStream outputStream = socket.getOutputStream();
        String msg = "{\"msgType\":\"req\",\"clientId\":\"请求数据\"}\r\n";
        outputStream.write(msg.getBytes(CharsetUtil.UTF_8));
        outputStream.flush();

        Thread.sleep(1000);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        char[] ch = new char[65536];
        int len = -1;
        while ((len = br.read(ch)) != -1) {
            String result = new String(ch, 0, len);
            System.out.println("原始消息：" + result); // 获取到的原始消息，是有粘包或者拆包的
            String[] results = result.split("\r\n"); // 按照\r\n拆分，这里没有处理拆包的问题
            for (String str : results) {
                System.out.println("接收到消息：" + str);
            }
        }
    }

}
