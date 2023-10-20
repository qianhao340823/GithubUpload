package org.example.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ChannelHandler.Sharable
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * @Description 客户端断开连接时执行，将客户端信息从Map中移除
     * @param ctx
     * @Date 2019/8/28 14:22
     * @Author wuyong
     * @return
     **/
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开连接：{}", getClientIp(ctx.channel()));
        NettyChannelMap.remove((SocketChannel) ctx.channel());
    }

    /**
     * @Description 客户端连接时执行，将客户端信息保存到Map中
     * @param ctx
     * @Date 2019/8/28 14:22
     * @Author wuyong
     * @return
     **/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有新的客户端连接：{}", getClientIp(ctx.channel()));
        String clientIp = getClientIp(ctx.channel());
        NettyClient client = new NettyClient((SocketChannel) ctx.channel(), getClientIp(ctx.channel()));
        NettyChannelMap.add(clientIp, client);
    }

    /**
     * @Description 收到消息时执行，根据消息类型做不同的处理
     * @param ctx
     * @param msg
     * @Date 2019/8/28 14:33
     * @Author wuyong
     * @return
     **/
    @Override
    public void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("收到客户端消息：" + msg);
        // 这个消息一般是结构化的数据，比如JSON字符串，解析这个JSON字符串，做相应的逻辑处理
        JSONObject msgObj = JSON.parseObject(msg);
        String msgType = msgObj.getString("msgType");
        switch (msgType) {
            // 回复客户端请求
            case "req":
                doReply(ctx);
                break;

            case "reply":
                doReplyLoop(ctx);
                break;

            default:
                break;
        }
    }

    /**
     * @description: TODO
     * @param ctx
     * @param cause
     * @Author: wuyong
     * @Date: 2019/08/30 13:41:51
     * @return: void
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("抛出异常执行，包括客户端断开连接时，会抛出IO异常");
    }

    /**
     * @description: 当收到客户端的消息后，进行处理
     * @param ctx
     * @Author: wuyong
     * @Date: 2019/08/30 14:10:59
     * @return: void
     */
    private void doReply(ChannelHandlerContext ctx) {
        String reply = "{\"msgType\":\"reply\",\"data\":\"回复的数据\"}";
        ctx.channel().writeAndFlush(reply);
    }
    private void doReplyLoop(ChannelHandlerContext ctx) {
        String reply = "{\"msgType\":\"reply\",\"data\":\"回复的数据\"}\r\n";
        for (int i = 0; i < 20; i++) {
            ctx.channel().writeAndFlush(reply);
        }
    }

    /**
     * @Description 获取客户端IP
     * @param channel
     * @Date 2019/8/28 14:32
     * @Author wuyong
     * @return
     **/
    private String getClientIp(Channel channel) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
        String clientIP = inetSocketAddress.getAddress().getHostAddress();
        return clientIP;
    }

    /**
     * @Description 当有新的客户端连接的时候，用于保存客户端信息
     * @Date 2019/8/28 14:20
     * @Author wuyong
     * @return
     **/
    public static class NettyChannelMap {

        public static Map<String, NettyClient> map = new ConcurrentHashMap<>();

        public static void add(String clientId, NettyClient client) {
            map.put(clientId, client);
        }

        public static NettyClient get(String clientId) {
            return map.get(clientId);
        }

        public static void remove(SocketChannel socketChannel) {
            for (Map.Entry entry : map.entrySet()) {
                if (((NettyClient) entry.getValue()).getChannel() == socketChannel) {
                    map.remove(entry.getKey());
                }
            }
        }
    }

    /**
     * @Description 封装客户端的信息
     * @Date 2019/8/28 14:21
     * @Author wuyong
     * @return
     **/
    @Data
    public static class NettyClient {

        /**客户端与服务器的连接*/
        private SocketChannel channel;

        /**ip地址*/
        private String clientIp;

        // ......

        public NettyClient(SocketChannel channel, String clientIp) {
            this.channel = channel;
            this.clientIp = clientIp;
        }

    }

}
