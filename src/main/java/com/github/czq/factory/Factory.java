/**
 * Alipay.com Inc. Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.github.czq.factory;

import com.github.czq.config.Config;
import com.github.czq.constants.EndpointConstants;
import com.github.czq.constants.PayConstants;
import com.github.czq.domain.UnifyOrderRequest;
import com.github.czq.endpoint.Client;
import com.github.czq.utils.Context;

/**
 * 客户端工厂，用于快速配置和访问各种场景下的API Client
 * <p>
 * 注：该Factory获取的Client不可储存重复使用，请每次均通过Factory完成调用
 * Factory.能力类别.支付平台.场景类别.连接方式.接口方法名称( ... )
 */
public class Factory {

    /**
     * 将一些初始化耗时较多的信息缓存在上下文中
     */
    private static Context context;

    /**
     * 设置客户端参数，只需设置一次，即可反复使用各种场景下的API Client
     *
     * @param options 客户端参数对象
     */
    public static void setOptions(Config options) {
        try {
            context = new Context(options);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * @param params 签名参数
     * @return true 成功
     * @throws Exception 签名异常
     */
    public static boolean checkSign(UnifyOrderRequest params) throws Exception {
        return new Client(context).checkSign(params);
    }

    /**
     * 支付能力相关
     */
    public static class Payment {
        /**
         * 获取支付通用API Client
         *
         * @return 支付通用API Client
         */
        public static Client Common() {
            return new Client(context);
        }

        /**
         * 支付宝
         */
        public static class Alipay {
            public static Client App() {
                return new Client(context).platform(PayConstants.Platform.ALIPAY).endpoint(EndpointConstants.Fields.APP);
            }

            public static Client H5() {
                return new Client(context).platform(PayConstants.Platform.ALIPAY).endpoint(EndpointConstants.Fields.H5);
            }

            public static Client Pc() {
                return new Client(context).platform(PayConstants.Platform.ALIPAY).endpoint(EndpointConstants.Fields.PC);
            }
        }

        /**
         * 花呗分期
         */
        public static class Instalments {
            public static Client H5() {
                return new Client(context).platform(PayConstants.Platform.INSTALMENTS).endpoint(EndpointConstants.Fields.H5);
            }

            public static Client Pc() {
                return new Client(context).platform(PayConstants.Platform.INSTALMENTS).endpoint(EndpointConstants.Fields.PC);
            }
        }

        /**
         * 微信
         */
        public static class Wechat {
            public static Client App() {
                return new Client(context).platform(PayConstants.Platform.WEIXIN).endpoint(EndpointConstants.Fields.APP);
            }

            public static Client H5() {
                return new Client(context).platform(PayConstants.Platform.WEIXIN).endpoint(EndpointConstants.Fields.H5);
            }

            public static Client Pc() {
                return new Client(context).platform(PayConstants.Platform.WEIXIN).endpoint(EndpointConstants.Fields.PC);
            }

            public static Client Wcoa() {
                return new Client(context).platform(PayConstants.Platform.WEIXIN).endpoint(EndpointConstants.Fields.WCOA);
            }

            public static Client wxprog() {
                return new Client(context).platform(PayConstants.Platform.WEIXIN).endpoint(EndpointConstants.Fields.WXPROG);
            }
        }

        /**
         * 融360
         */
        public static class rong360 {
            public static Client H5() {
                return new Client(context).platform(PayConstants.Platform.RONG360).endpoint(EndpointConstants.Fields.H5).connType(PayConstants.ConnType.INDIRECT);
            }

            public static Client Pc() {
                return new Client(context).platform(PayConstants.Platform.RONG360).endpoint(EndpointConstants.Fields.PC).connType(PayConstants.ConnType.INDIRECT);
            }
        }

        /**
         * 百度分期
         */
        public static class Umoney {
            public static Client Pc() {
                return new Client(context).platform(PayConstants.Platform.UMONEY).endpoint(EndpointConstants.Fields.PC).connType(PayConstants.ConnType.INDIRECT);
            }
        }
    }
}