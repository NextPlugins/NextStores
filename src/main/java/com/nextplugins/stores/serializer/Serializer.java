package com.nextplugins.stores.serializer;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public interface Serializer<T> {

    T decode(String data);

    String encode(T data);
}
