package cn.xzxy.lewy.netty.furtherimpl;

import cn.xzxy.lewy.netty.furtherimpl.assembly.LengthRestrictedFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 服务器端启动类
 */
public class ServerApp {

    private static final int maxBytesLength = 20480;

    private int port;

    private ServerApp(int port) {
        this.port = port;
    }

    private void run() throws Exception {
        // Netty的reactor线程池，初始化了一个NioEventLoop数组，用来处理IO操作
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap(); // 用于启动NIO服务
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port)) // 设置监听端口
                    // ChannelInitializer 是一个特殊的处理类
                    // 目的是帮助使用者配置一个新的Channel，用于把自定义的处理类添加到pipeline中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 配置 childHandler 来通知一个关于消息处理的InfoServerHandler实例
                            socketChannel.pipeline()
                                    //.addLast(new LengthRestrictedFrameDecoder(maxBytesLength))
                                    .addLast(new ServerHandler());
                        }
                    });
            // 绑定服务器，该实例将提供有关IO操作的结果或状态的信息
            ChannelFuture channelFuture = bootstrap.bind().sync();
            System.out.println("open listener on " + channelFuture.channel().localAddress());

            // 阻塞操作，closeFuture()开启了一个channel的监听器（期间channel在进行各项作用），知道链路断开
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup并释放所有资源，包括所有创建的线程
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new ServerApp(12915).run();
    }
}
