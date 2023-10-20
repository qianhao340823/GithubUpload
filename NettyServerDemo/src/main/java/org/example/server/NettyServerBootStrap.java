package org.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NettyServerBootStrap {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    public void start() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 使消息立即发出去，不用等待到一定的数据量才发出去
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 保持长连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));
                            p.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            p.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            p.addLast(nettyServerHandler);
                        }
                    });
            // 绑定端口，同步等待成功
            ChannelFuture f = bootstrap.bind(5678).sync();
            if (f.isSuccess()) {
                log.info("Netty Start successful");
            } else {
                log.error("Netty Start failed");
            }
            // 等待服务监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 退出，释放线程资源
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

}
