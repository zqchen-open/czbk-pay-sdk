package com.github.czq.endpoint;


import com.alibaba.fastjson.JSONObject;
import com.github.czq.constants.EndpointConstants;
import com.github.czq.constants.PayConstants;
import com.github.czq.domain.PayResponse;
import com.github.czq.domain.UnifyOrderRequest;
import com.github.czq.tea.TeaConverter;
import com.github.czq.tea.TeaPair;
import com.github.czq.utils.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class Client extends AbstractPayment {
    public Context _kernel;
    private String connType;
    private String platform;
    private String endpoint;
    private String hbFqNum;

    public Client(Context kernel) {
        this._kernel = kernel;
    }

    /**
     * 统一下支付单
     *
     * @param params 下单参数
     * @return 下单结果
     * @throws Exception 签名异常
     */
    public PayResponse pay(UnifyOrderRequest params) throws Exception {
        Assert.hasText(connType, "连接方式不能为空");
        Assert.hasText(platform, "平台不能为空");
        Assert.hasText(endpoint, "终端不能为空");
        Map<String, Object> systemParams = systemParams();
        Map<String, Object> systemExtendParams = systemExtendParams();
        Map<String, Object> bizParams = bizParams(params);
        Map<String, Object> bizExtendParams = bizExtendParams(params);
        String sign = _kernel.sign(systemParams, systemExtendParams, bizParams, bizExtendParams, _kernel.getConfig("privateKey"));
        JSONObject jsonObject = _kernel.sendPay(systemParams, systemExtendParams, bizParams, bizExtendParams, sign);
        log.info("统一下支付单接收参数：{}", jsonObject);
        return jsonObject.toJavaObject(PayResponse.class);
    }

    /**
     * 统一下退款下单
     *
     * @param params 下单参数
     * @return 下单结果
     * @throws Exception 签名异常
     */
    public PayResponse refund(UnifyOrderRequest params) throws Exception {
        Assert.hasText(platform, "平台不能为空");
        if (!PayConstants.Platform.CONN_TYPES.contains(platform)) {
            throw new RuntimeException("暂不支持'" + platform + "'平台退款");
        }
        Map<String, Object> systemParams = systemParams();
        Map<String, Object> systemExtendParams = systemExtendParams();
        Map<String, Object> bizParams = bizParams(params);
        Map<String, Object> bizExtendParams = bizExtendParams(params);
        String sign = _kernel.sign(systemParams, systemExtendParams, bizParams, bizExtendParams, _kernel.getConfig("privateKey"));
        JSONObject jsonObject = _kernel.sendRefund(systemParams, systemExtendParams, bizParams, bizExtendParams, sign);
        log.info("统一下退款单接收参数：{}", jsonObject);
        return jsonObject.toJavaObject(PayResponse.class);
    }

    /**
     * 签名
     *
     * @param params 签名参数
     * @return 签名结果
     * @throws Exception 签名异常
     */
    public boolean checkSign(UnifyOrderRequest params) throws Exception {
        Map<String, Object> systemParams = systemParams();
        Map<String, Object> systemExtendParams = systemExtendParams();
        Map<String, Object> bizParams = bizParams(params);
        Map<String, Object> bizExtendParams = bizExtendParams(params);
        boolean sign = _kernel.checkSign(systemParams, systemExtendParams, bizParams, bizExtendParams, _kernel.getConfig("privateKey"));
        return sign;
    }

    /**
     * 业务请求扩展参数
     *
     * @param params
     */
    private Map<String, Object> bizExtendParams(UnifyOrderRequest params) {
        return TeaConverter.buildMap(
                new TeaPair("extend1", params.getExtend1()),
                new TeaPair("extend2", params.getExtend2()),
                new TeaPair("extend3", params.getExtend3()),
                new TeaPair("openid", params.getOpenid()),
                new TeaPair("passback_param", params.getPassbackParam())
        );
    }

    /**
     * 业务请求参数
     *
     * @param params
     * @return
     */
    private Map<String, Object> bizParams(UnifyOrderRequest params) {
        return TeaConverter.buildMap(
                new TeaPair("merchant_order_no", params.getMerchantOrderNo()),
                new TeaPair("total_amount", params.getTotalAmount()),
                new TeaPair("original_price", params.getOriginalPrice()),
                new TeaPair("discount_amount", params.getDiscountAmount()),
                new TeaPair("product_id", params.getProductId()),
                new TeaPair("product_name", params.getProductName()),
                new TeaPair("product_detail_url", params.getProductDetailUrl()),
                new TeaPair("order_desc", params.getOrderDesc()),
                new TeaPair("currency", "CNY"),
                new TeaPair("client_type", endpoint),
                new TeaPair("conn_type", StringUtils.hasText(connType) ? connType : PayConstants.ConnType.INDIRECT),
                new TeaPair("client_ip", params.getClientIp()),
                new TeaPair("payment_method", platform)
        );
    }

    /**
     * 公共请求扩展参数
     *
     * @return
     */
    private Map<String, Object> systemExtendParams() {
        return TeaConverter.buildMap(
                new TeaPair("return_url", _kernel.getConfig("returnUrl")),
                new TeaPair("notify_url", _kernel.getConfig("notifyUrl"))
        );
    }

    /**
     * 公共请求参数
     *
     * @return
     */
    private Map<String, Object> systemParams() {
        return TeaConverter.buildMap(
                new TeaPair("app_id", _kernel.getConfig("appId")),
                new TeaPair("timestamp", _kernel.getTimestamp()),
                new TeaPair("format", "json"),
                new TeaPair("version", "1.0"),
                new TeaPair("charset", StandardCharsets.UTF_8.name())
        );
    }

    /**
     * 连接方式:直连-DIRECT;非直连-INDIRECT;
     * 默认非直连-INDIRECT
     * <p>
     * {@link PayConstants.ConnType#DIRECT}
     * {@link PayConstants.ConnType#INDIRECT}
     *
     * @param connType 连接方式
     * @return 支付处理
     */
    @Override
    public Client connType(String connType) {
        this.connType = connType;
        return this;
    }

    /**
     * 直连
     *
     * @return 支付处理
     */
    public Client connTypeDirect() {
        connType = PayConstants.ConnType.DIRECT;
        return this;
    }

    /**
     * 非直连
     *
     * @return 支付处理
     */
    public Client connTypeIndirect() {
        connType = PayConstants.ConnType.INDIRECT;
        return this;
    }

    /**
     * 平台
     * <p>
     * {@link PayConstants.Platform#ALIPAY}
     * {@link PayConstants.Platform#WEIXIN}
     *
     * @param platform 平台
     * @return 支付处理
     */
    @Override
    public Client platform(String platform) {
        this.platform = platform;
        return this;
    }

    /**
     * 终端
     * {@link EndpointConstants#APP}
     * {@link EndpointConstants#PC}
     * {@link EndpointConstants#H5}
     * {@link EndpointConstants#WCOA}
     * {@link EndpointConstants#WXPROG}
     *
     * @param endpoint 终端
     * @return 支付处理
     */
    @Override
    public Client endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    /**
     * 分期期数
     * <p>
     * {@link PayConstants.HbFqNum#ALIPAY_6}
     * {@link PayConstants.HbFqNum#RONG360_6_12}
     *
     * @param hbFqNum 分期期数
     * @return 支付处理
     */
    public Client hbFqNum(String hbFqNum) {
        this.hbFqNum = hbFqNum;
        return this;
    }

}