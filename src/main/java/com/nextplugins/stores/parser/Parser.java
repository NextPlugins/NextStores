package com.nextplugins.stores.parser;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public interface Parser<T> {

    T decode(String data);

    String encode(T data);

}
