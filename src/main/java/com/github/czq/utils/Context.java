/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.github.czq.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.czq.config.Config;
import com.github.czq.tea.TeaModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class Context {
    /**
     * 客户端配置参数
     */
    private final Map<String, Object> config;


    public Context(Config options) throws Exception {
        this.config = TeaModel.buildMap(options);
    }

    /**
     * 获取时间戳，格式yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间戳
     */
    public String getTimestamp() {
        return String.valueOf((System.currentTimeMillis() / 1000));
    }

    /**
     * @param systemParams       公共请求参数
     * @param systemExtendParams 业务请求扩展参数
     * @param bizParams          业务请求参数
     * @param bizExtendParams    公共请求扩展参数
     * @param privateKey         私钥
     * @return 签名
     * @throws Exception 签名异常
     */
    public String sign(Map<String, Object> systemParams, Map<String, Object> systemExtendParams, Map<String, Object> bizParams, Map<String, Object> bizExtendParams, String privateKey) throws Exception {
        Map<String, Object> sortedMap = this.getSortedMap(systemParams, systemExtendParams, bizParams, bizExtendParams);
        return RSA2ForMchtUtils.RSA2Sign(sortedMap, privateKey);
    }

     public boolean checkSign(Map<String, Object> systemParams, Map<String, Object> systemExtendParams, Map<String, Object> bizParams, Map<String, Object> bizExtendParams, String privateKey) throws Exception {
        Map<String, Object> sortedMap = this.getSortedMap(systemParams, systemExtendParams, bizParams, bizExtendParams);
        return RSA2ForMchtUtils.rsa2Check(sortedMap, privateKey);
    }



    /**
     *
     * @param systemParams 系统参数集合
     * @param systemExtendParams 系统额外参数集合
     * @param bizParams 业务参数集合
     * @param bizExtendParams 业务额外参数集合
     * @param sign 所有参数的签名值
     * @return 下单参数
     * @throws Exception 下单异常
     */
    public JSONObject sendPay(Map<String, Object> systemParams, Map<String, Object> systemExtendParams, Map<String, Object> bizParams, Map<String, Object> bizExtendParams, String sign) throws Exception {
        Map<String, Object> params = this.getSortedMap(systemParams, systemExtendParams, bizParams, bizExtendParams);
        params.put("sign", sign);
        log.info("统一支付下单请求参数：{}", JSON.toJSONString(params));
        String url = this.payUrl();
        return post(params, url);
    }

    /**
     *
     * @param systemParams 系统参数集合
     * @param systemExtendParams 系统额外参数集合
     * @param bizParams 业务参数集合
     * @param bizExtendParams 业务额外参数集合
     * @param sign 所有参数的签名值
     * @return 下单参数
     * @throws Exception 下单异常
     */
    public JSONObject sendRefund(Map<String, Object> systemParams, Map<String, Object> systemExtendParams, Map<String, Object> bizParams, Map<String, Object> bizExtendParams, String sign) throws Exception {
        Map<String, Object> params = this.getSortedMap(systemParams, systemExtendParams, bizParams, bizExtendParams);
        params.put("sign", sign);
        log.info("统一退款下单请求参数：{}", JSON.toJSONString(params));
        String url = this.refundUrl();
        return post(params, url);
    }


    private JSONObject post(Map<String, Object> params, String url) throws UnsupportedEncodingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            params.put(entry.getKey(), URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        }
        HttpEntity httpEntity = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<JSONObject> responseEntity = RestTemplateUtils.getRestTemplate().postForEntity(url, httpEntity, JSONObject.class);
        return responseEntity.getBody();
    }
    /**
     * @param systemParams       公共请求参数
     * @param systemExtendParams 业务请求扩展参数
     * @param bizParams          业务请求参数
     * @param bizExtendParams    公共请求扩展参数
     * @return 组装参数
     * @throws Exception 组装参数异常
     */
    private Map<String, Object> getSortedMap(Map<String, Object> systemParams, Map<String, Object> systemExtendParams, Map<String, Object> bizParams, Map<String, Object> bizExtendParams) throws Exception {
        // 公共请求参数
        Map<String, Object> sortedMap = new TreeMap(systemParams);
        if (bizParams != null && !bizParams.isEmpty()) {
            // 业务请求扩展参数
            if (bizExtendParams != null) {
                bizParams.putAll(bizExtendParams);
            }
            // 业务请求参数
            sortedMap.put("biz_params", JSON.toJSONString(bizParams));
        }
        // 公共请求扩展参数
        if (systemExtendParams != null) {
            sortedMap.putAll(systemExtendParams);
        }
        return sortedMap;
    }

    public String getConfig(String key) {
        return (String) config.get(key);
    }

    private String payUrl() {
        return "https://" + this.getConfig("gatewayHost") + "/gateway/unifyOrder";
    }

    private String refundUrl() {
        return "https://" + this.getConfig("gatewayHost") + "/gateway/refund";
    }

}