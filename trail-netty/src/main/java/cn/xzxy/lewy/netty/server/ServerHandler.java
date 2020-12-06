package cn.xzxy.lewy.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 处理收到的数据，并反馈消息到客户端
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("receive message from client: " + in.toString(CharsetUtil.UTF_8));
        // 数据写入并发送到客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, i am server, i have got your message", CharsetUtil.UTF_8));
    }

    /**
     * 处理IO事件异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
