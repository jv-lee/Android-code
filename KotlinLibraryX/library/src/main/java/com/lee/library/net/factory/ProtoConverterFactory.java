package com.lee.library.net.factory;

import androidx.annotation.NonNull;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} which uses Protocol Buffers.
 * <p>
 * This converter only applies for types which extend from {@link MessageLite} (or one of its
 * subclasses).
 */
public final class ProtoConverterFactory extends Converter.Factory {
    public static ProtoConverterFactory create() {
        return new ProtoConverterFactory(null);
    }

    /**
     * Create an instance which uses {@code registry} when deserializing.
     */
    public static ProtoConverterFactory createWithRegistry(@Nullable ExtensionRegistryLite registry) {
        return new ProtoConverterFactory(registry);
    }

    private final @Nullable
    ExtensionRegistryLite registry;

    private ProtoConverterFactory(@Nullable ExtensionRegistryLite registry) {
        this.registry = registry;
    }

    @Override
    public @Nullable
    Converter<ResponseBody, ?> responseBodyConverter(
            @NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (!(type instanceof Class<?>)) {
            return null;
        }
        Class<?> c = (Class<?>) type;
        if (!MessageLite.class.isAssignableFrom(c)) {
            return null;
        }

        Parser<MessageLite> parser;
        try {
            Method method = c.getDeclaredMethod("parser");
            //noinspection unchecked
            parser = (Parser<MessageLite>) method.invoke(null);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } catch (NoSuchMethodException | IllegalAccessException ignored) {
            // If the method is missing, fall back to original static field for pre-3.0 support.
            try {
                Field field = c.getDeclaredField("PARSER");
                //noinspection unchecked
                parser = (Parser<MessageLite>) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Found a protobuf message but "
                        + c.getName()
                        + " had no parser() method or PARSER field.");
            }
        }
        return new ProtoResponseBodyConverter<>(parser, registry);
    }

    @Override
    public @Nullable
    Converter<?, RequestBody> requestBodyConverter(
            @NonNull Type type,
            @NonNull Annotation[] parameterAnnotations,
            @NonNull Annotation[] methodAnnotations,
            @NonNull Retrofit retrofit) {
        if (!(type instanceof Class<?>)) {
            return null;
        }
        if (!MessageLite.class.isAssignableFrom((Class<?>) type)) {
            return null;
        }
        return new ProtoRequestBodyConverter<>();
    }


    static final class ProtoRequestBodyConverter<T extends MessageLite> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.get("application/x-protobuf");

        @Override
        public RequestBody convert(T value) throws IOException {
            byte[] bytes = value.toByteArray();
            return RequestBody.create(MEDIA_TYPE, bytes);
        }
    }

    static final class ProtoResponseBodyConverter<T extends MessageLite>
            implements Converter<ResponseBody, T> {
        private final Parser<T> parser;
        private final @Nullable
        ExtensionRegistryLite registry;

        ProtoResponseBodyConverter(Parser<T> parser, @Nullable ExtensionRegistryLite registry) {
            this.parser = parser;
            this.registry = registry;
        }

        @Override
        public T convert(@NonNull ResponseBody value) throws IOException {
            try {
                return registry == null ? parser.parseFrom(value.byteStream())
                        : parser.parseFrom(value.byteStream(), registry);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e); // Despite extending IOException, this is data mismatch.
            } finally {
                value.close();
            }
        }
    }

}



