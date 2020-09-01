package com.github.czq.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 统一下单业务请求参数
 */
@Data
@Builder
public class UnifyOrderRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String merchantOrderNo;

    /**
     * 订单应金额，单位为元，精确到小数点后两位
     */
    private BigDecimal totalAmount;

    /**
     * 订单原价，单位为元，精确到小数点后两位
     */
    private BigDecimal originalPrice;

    /**
     * 订单优惠金额，单位为元，精确到小数点后两位
     */
    private BigDecimal discountAmount;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 订单包含的商品链接地址
     */
    private String productDetailUrl;

    /**
     * 订单描述
     */
    private String orderDesc;

    /**
     * 提交用户端的ip地址
     */
    private String clientIp;

    /**
     * 扩展字段1
     */
    private String extend1;

    /**
     * 扩展字段2
     */
    private String extend2;

    /**
     * 扩展字段3
     */
    private String extend3;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 自定义参数
     */
    private String passbackParam;

}
