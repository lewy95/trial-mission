package cn.xzxy.lewy.netty.furtherimpl.assembly;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

public class LengthRestrictedFrameDecoder extends ByteToMessageDecoder {

    private int maxBytesLength;

    public LengthRestrictedFrameDecoder(int maxBytesLength) {
        this.maxBytesLength = maxBytesLength;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        if (maxBytesLength <= 0) {
            throw new IllegalArgumentException("Invalid max Bytes length");
        }

        // byteBuf.markReaderIndex 获取当前可读的缓冲区
        // readableBytes.readableBytes 获取缓冲区中字节数
        byteBuf.markReaderIndex();
        if (byteBuf.readableBytes() < 2) {
            // resetReaderIndex 重置缓冲区中可读的索引
            byteBuf.resetReaderIndex();
        }

        // 将当前readIndex处的无符号short值作为int返回，并将readIndex增加2
        int length = byteBuf.readUnsignedShort();
        if (length > byteBuf.readableBytes()) {
            byteBuf.resetReaderIndex();
        }

        if (length > maxBytesLength) {
            throw new TooLongFrameException("length: " + length);
        } else {
            // 返回当前ByteBuf新创建的子区域，子区域和原ByteBuf共享缓冲区的内容，
            // 但独立维护自己的readerIndex和writeIndex，新创建的子区域readerIndex 为0，writeIndex为length
            list.add(byteBuf.readSlice(length).retain());
            // retain方法，目标是让引用计数器加一，避免新的ByteBuf被回收
        }
    }
}
