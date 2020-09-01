package com.github.czq.utils;


import java.util.HashMap;

/**
 * 全局返回码（禁止将业务code码定义在此类）
 */
public enum CzbkWebStatus implements CzbkStatus {
    /**
     * 成功
     */
    SUCCESS(0, "Success"),
    /**
     * 失败
     */
    FAILURE(1, "Failure"),
    /**
     * 未知异常,请联系管理员!
     */
    ERROR(500, "系统异常,请联系管理员!"),
    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "请求参数错误!"),
    /**
     * 无权限访问
     */
    NO_ACCESS(1001, "无权限访问"),
    /**
     * 数据不存在
     */
    NO_DATA(10001, "数据不存在"),
    /**
     * 数据已存在
     */
    DATA_EXIST(10002, "数据已存在"),
    /**
     * 下单失败
     */
    EXCEPTION_CODE_5001(5001, "下单失败"),
    EXCEPTION_CODE_5002(5002, "签名失败"),
    EXCEPTION_CODE_5003(5003, "对不起,不支持该支付方式,请选择其他支付方式"),
    ;

    private int code;
    private String msg;

    CzbkWebStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    private static HashMap<Integer, CzbkWebStatus> map = new HashMap<>();

    static {
        for (CzbkWebStatus d : CzbkWebStatus.values()) {
            map.put(d.code, d);
        }
    }

    public static CzbkWebStatus parse(int code) {
        if (map.containsKey(code)) {
            return map.get(code);
        }
        return null;
    }


}
