package com.haodong.practice.lib_network;

import java.lang.reflect.Type;

/**
 * created by linghaoDo on 2020/9/8
 * description:
 * <p>
 * version:
 */
public interface Convert<T> {
    T convert(String response, Type type);
}
