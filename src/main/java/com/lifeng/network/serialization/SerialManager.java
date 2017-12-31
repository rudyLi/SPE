package com.lifeng.network.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

public class SerialManager {
    private static final Kryo kryo = new Kryo();

    public static void ser(Object object, ByteBuf byteBuf) {
        Output outputStream = new Output(new ByteBufOutputStream(byteBuf));
        kryo.writeObject(outputStream, object);
        outputStream.close();
    }

    public static <T> T deser(ByteBuf byteBuf, Class<T> type) {
        Input input = new Input(new ByteBufInputStream(byteBuf));
        T result = kryo.readObject(input, type);
        input.close();
        return result;
    }

}
