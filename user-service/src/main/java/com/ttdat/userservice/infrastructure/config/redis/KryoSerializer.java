package com.ttdat.userservice.infrastructure.config.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class KryoSerializer {

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);

        return kryo;
    });

    public byte[] serialize(Object o) {
        if (o == null) {
            return new byte[]{};
        }

        Kryo kryo = kryoThreadLocal.get();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeClassAndObject(output, o);
        output.close();

        return outputStream.toByteArray();
    }

    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        Kryo kryo = kryoThreadLocal.get();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(inputStream);
        var o = kryo.readClassAndObject(input);
        input.close();

        return o;
    }
}
