package org.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NettyClientBootStrap {

    private static final String HOST = "localhost";
    private static final int PORT = 5678;
    private static SocketChannel socketChannel = null;

    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(HOST, PORT);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));
                socketChannel.pipeline().addLast(new IdleStateHandler(20, 10, 0));
                socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });
        ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
        if (future.isSuccess()) {
            socketChannel = (SocketChannel) future.channel();
            log.info("connect server success");
        }
    }

    public static SocketChannel getSocketChannel() {
        return socketChannel;
    }

}
