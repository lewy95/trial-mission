package cn.xzxy.lewy.netty.furtherimpl;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 客户端启动类
 */
public class ClientApp {

    private final String host;
    private final int port;

    private ClientApp(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(); // IO线程池
        // 配置客户端启动辅助对象
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class) // 实例化一个channel
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() { // 进行通过初始化配置

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 添加自定义的handler，基于pipeline1的责任链模式
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });

            // 通过事件机制获取执行结果
            // 连接到远程结点，等待连接完成
            ChannelFuture future = bootstrap.connect().sync();

            // 发送消息到服务器端
            future.channel().writeAndFlush(Unpooled.copiedBuffer("Hello oo, Hello oo, Hello oo, Hello oo, Hello oo,",
                    CharsetUtil.UTF_8));

            // 阻塞操作，closeFuture()开启了一个channel的监听器
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new ClientApp("127.0.0.1", 12915).run();
    }
}
