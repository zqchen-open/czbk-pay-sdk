/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.github.czq.config;

import com.github.czq.tea.TeaModel;

public class Config extends TeaModel {
    /**
     * 网关域名
     */
    public String gatewayHost;
    /**
     * AppId
     */
    public String appId;
    /**
     * 应用私钥
     */
    public String privateKey;
    /**
     * 异步通知回调地址（可选）
     */
    public String notifyUrl;
    /**
     * 同步通知回调地址（可选）
     */
    public String returnUrl;

}