package com.haodong.practice.lib_network;

/**
 * 请求结果返回包裹类
 * @param <T> 接口返回数据的数据结构
 */
public class ApiResponse<T> {
    public boolean success;
    public int status;
    public String message;
    public T body;
}
