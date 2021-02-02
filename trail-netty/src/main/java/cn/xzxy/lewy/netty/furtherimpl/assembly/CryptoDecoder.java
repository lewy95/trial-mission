package cn.xzxy.lewy.netty.furtherimpl.assembly;

import cn.xzxy.lewy.netty.furtherimpl.assembly.util.AES;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CryptoDecoder extends MessageToMessageDecoder<ByteBuf> {

    private static AttributeKey<AES> crypto = AttributeKey.valueOf("crypto");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> list) throws Exception {

        if (ctx.channel().attr(crypto).get() != null) {
            byte[] msgBytes = new byte[msg.readableBytes()];
            msg.readBytes(msgBytes);
            log.info("Decrypt, len={}, bytes=${}", msgBytes.length, StringUtil.toHexString(msgBytes));
            try {
                byte[] msgBytes2 = ctx.channel().attr(crypto).get().decrypt(msgBytes);
                list.add(Unpooled.wrappedBuffer(msgBytes2));
            } catch (Throwable throwable){
                log.error("Decrypt error, e={}, channel={}, bytes={}", throwable.getMessage(),
                        ctx.channel(), StringUtil.toHexString(msgBytes));
                ctx.close();
            }
        } else {
            msg.retain();
            list.add(msg);
        }
    }
}
