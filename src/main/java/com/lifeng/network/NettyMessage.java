package com.lifeng.network;

import com.google.common.base.Preconditions;
import com.lifeng.network.serialization.SerialManager;
import com.lifeng.tuple.Tuple;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.List;

public abstract class NettyMessage {
    static final int HEADER_LENGTH = 4 + 4 + 1; // frame length (4), magic number (4), msg ID (1)

    static final int MAGIC_NUMBER = 0xBADC0FFE;

    abstract ByteBuf write(ByteBufAllocator allocator) throws Exception;
    private static ByteBuf allocateBuffer(ByteBufAllocator allocator, byte id) {
        return allocateBuffer(allocator, id, -1);
    }

    private static ByteBuf allocateBuffer(ByteBufAllocator allocator, byte id, int length) {
        Preconditions.checkArgument(length <= Integer.MAX_VALUE - HEADER_LENGTH);

        final ByteBuf buffer;
        if (length != -1) {
            buffer = allocator.directBuffer(HEADER_LENGTH + length);
        } else {
            // content length unknown -> start with the default initial size (rather than HEADER_LENGTH only):
            buffer = allocator.directBuffer();
        }

        buffer.writeInt(HEADER_LENGTH + length);
        buffer.writeInt(MAGIC_NUMBER);
        buffer.writeByte(id);

        return buffer;
    }

    @ChannelHandler.Sharable
    static class NettyMessageEncoder extends ChannelOutboundHandlerAdapter {

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            if (msg instanceof NettyMessage) {

                ByteBuf serialized = null;

                try {
                    serialized = ((NettyMessage) msg).write(ctx.alloc());
                } catch (Throwable t) {
                    throw new IOException("Error while serializing message: " + msg, t);
                } finally {
                    if (serialized != null) {
                        ctx.write(serialized, promise);
                    }
                }
            } else {
                ctx.write(msg, promise);
            }
        }

        // Create the frame length decoder here as it depends on the encoder
        //
        // +------------------+------------------+--------++----------------+
        // | FRAME LENGTH (4) | MAGIC NUMBER (4) | ID (1) || CUSTOM MESSAGE |
        // +------------------+------------------+--------++----------------+
        static LengthFieldBasedFrameDecoder createFrameLengthDecoder() {
            return new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, -4, 4);
        }
    }
    @ChannelHandler.Sharable
    static class NettyMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
            int magicNumber = msg.readInt();

            if (magicNumber != MAGIC_NUMBER) {
                throw new IllegalStateException("Network stream corrupted: received incorrect magic number.");
            }

            byte msgId = msg.readByte();

            final NettyMessage decodedMsg;
            switch (msgId) {
                case HeartBeatMessage.ID:
                    decodedMsg = HeartBeatMessage.readFrom(msg);
                    break;
                case StreamMessage.ID:
                    decodedMsg = StreamMessage.readFrom(msg);
                    break;
                default:
                    throw new ProtocolException("Received unknown message from producer: " + msg);
            }

            out.add(decodedMsg);
        }
    }

    public static class HeartBeatMessage extends NettyMessage{
        private static final byte ID = 0;

        private static final HeartBeatMessage instance = new HeartBeatMessage();

        static HeartBeatMessage getInstance() {
            return instance;
        }

        @Override
        ByteBuf write(ByteBufAllocator allocator) throws Exception {
            return NettyMessage.allocateBuffer(allocator, ID, 0);
        }

        static HeartBeatMessage readFrom(ByteBuf byteBuf) {
            return instance;
        }
    }

    public static class StreamMessage extends NettyMessage {
        private static final byte ID = 1;

        public Tuple tuple;
        @Override
        ByteBuf write(ByteBufAllocator allocator) throws Exception {
            ByteBuf byteBuf = NettyMessage.allocateBuffer(allocator, ID);
            int startIndex = byteBuf.writerIndex();
            SerialManager.ser(tuple, byteBuf);
            int endIndex = byteBuf.writerIndex();
            byteBuf.setInt(startIndex, endIndex - startIndex);
            return byteBuf;
        }
        static StreamMessage readFrom(ByteBuf byteBuf) {
            return SerialManager.deser(byteBuf, StreamMessage.class);

        }
    }


}
