/**
 * Alipay.com Inc. Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.pay;


import com.github.czq.config.Config;


public class TestAccount {

    public static class Configs {
        public static final Config CONFIG = getConfig();

        public static Config getConfig() {
            Config config = new Config();
            config.gatewayHost = "";
            config.appId = "";
            // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
            config.privateKey = "";
            String returnUrl = "";
            String notifyUrl = "";
            config.notifyUrl = notifyUrl;
            config.returnUrl = returnUrl;
            return config;
        }
    }
}