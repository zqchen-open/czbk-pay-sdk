package com.github.czq.utils;

/**
 * 后续本服务的异常自己定义，只要继承这个接口即可，具体可以看{@link CzbkWebStatus}
 */
public interface CzbkStatus {

    int getCode();

    String getMsg();

}
